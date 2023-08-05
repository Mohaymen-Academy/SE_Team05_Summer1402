package ir.shelmossenger.repositories;

import ir.shelmossenger.model.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static ir.shelmossenger.context.DbContext.getConnection;

public class MessageRepo {

    public boolean sendMessage(String messageContent, String messageType, String senderUserName, long chatId)
            throws SQLException {
        Chat chat = new Chat() {{
            setId(chatId);
        }};
        User user = new User() {{
            setUsername(senderUserName);
        }};
        Message message = new Message() {{
            setMessageType(MessageType.getByTitle(messageType));
            setData(messageContent);
            setChat(chat);
            setSender(user);
        }};
        Session session = getConnection();
        session.beginTransaction();
        session.persist(message);
        session.getTransaction().commit();
        session.close();
        return message.getId() != 0;
    }

    public void editMessage(String newMessage, long messageId)
            throws SQLException {
        Session session = getConnection();
        session.beginTransaction();
        var message = session.get(Message.class, messageId);
        message.setData(newMessage);
        session.persist(message);
        session.getTransaction().commit();
        session.close();
    }

    public void deleteMessage(long messageId)
            throws SQLException {
        Session session = getConnection();
        session.beginTransaction();
        var message = session.get(Message.class, messageId);
        message.setDeletedAt(Instant.now());
        session.persist(message);
        session.getTransaction().commit();
        session.close();
    }

    //TODO:
    public List<Message> getMessagesOfUser(String userName)
            throws SQLException {
        Session session = getConnection();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> subquery = cb.createQuery(Long.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(userRoot.get("id"))
                .where(cb.equal(userRoot.get("username"), userName));

        CriteriaQuery<Message> criteria = cb.createQuery(Message.class);
        Root<Message> messageRoot = criteria.from(Message.class);
        criteria.select(messageRoot)
                .where(cb.and(
                        cb.equal(messageRoot.get("senderId"), subquery),
                        cb.isNull(messageRoot.get("deletedAt"))
                ));

        List<Message> messages = session.createQuery(criteria).getResultList();
        session.close();
        return messages;
    }

    public long getNumberOfMessagesOfUser(String userName)
            throws SQLException {
        Session session = getConnection();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> subquery = cb.createQuery(Long.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(userRoot.get("id"))
                .where(cb.equal(userRoot.get("username"), userName));

        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Message> messageRoot = criteria.from(Message.class);
        criteria.select(cb.count(messageRoot))
                .where(cb.and(
                        cb.equal(messageRoot.get("senderId"), subquery),
                        cb.isNull(messageRoot.get("deletedAt"))
                ));

        Long count = session.createQuery(criteria).getSingleResult();
        session.close();
        return count;
    }

    public double getAvgNumberOfMessages()
            throws SQLException {
        Session session = getConnection();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> subquery = cb.createQuery(Long.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(cb.count(userRoot))
                .where(cb.isNull(userRoot.get("deletedAt")));
        Long countUsers = session.createQuery(subquery).getSingleResult();

        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Message> messageRoot = criteria.from(Message.class);
        criteria.select(cb.count(messageRoot))
                .where(cb.isNull(messageRoot.get("deletedAt")));

        Long countMessages = session.createQuery(criteria).getSingleResult();
        session.close();
        return (double) countMessages / countUsers;
    }

    public long getNumberOfViewsOfMessage(long messageId)
            throws SQLException {
        Session session = getConnection();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<ReadMessage> messageRoot = criteria.from(ReadMessage.class);
        criteria.select(cb.count(messageRoot))
                .where(cb.equal(messageRoot.get("messageId"), messageId));

        Long countViews = session.createQuery(criteria).getSingleResult();
        session.close();
        return countViews;
    }

    public boolean readMessage(String userName, long messageId) {
        Session session = getConnection();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> subquery = cb.createQuery(Long.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(userRoot.get("id"))
                .where(cb.equal(userRoot.get("username"), userName));
        long userId = session.createQuery(subquery).getSingleResult();
        ReadMessage readMessage = new ReadMessage() {{
            setMessage(new Message() {{
                setId(messageId);
            }});
            setUser(new User() {{
                setId(userId);
            }});
        }};
        session.persist(readMessage);
        session.getTransaction().commit();
        session.close();

        return true;
    }
}
