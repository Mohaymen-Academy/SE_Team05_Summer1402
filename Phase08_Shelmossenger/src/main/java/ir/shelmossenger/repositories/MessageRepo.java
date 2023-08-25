package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.Instant;
import java.util.List;

public class MessageRepo {

    private static MessageRepo messageRepo;

    private MessageRepo() {
    }

    public static MessageRepo getInstance() {
        if (messageRepo == null) messageRepo = new MessageRepo();
        return messageRepo;
    }

    public boolean sendMessage(String messageContent, MessageType messageType, String senderUserName, long chatId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            CriteriaQuery<User> userByUsernameQuery = UserRepo.getUserByUsernameQuery(session, senderUserName);
            User user = session.createQuery(userByUsernameQuery).getSingleResult();

            Chat chat = session.get(Chat.class, chatId);

            CriteriaQuery<UserChat> userChatByUerIdAndChatId = getUserChatByUserAndChat(session, user, chat);
            session.createQuery(userChatByUerIdAndChatId).getSingleResult();

            Message message = Message.builder()
                    .chat(chat)
                    .sender(user)
                    .data(messageContent)
                    .messageType(messageType).build();

            session.persist(message);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean editMessage(String newMessage, long messageId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            Message message = session.get(Message.class, messageId);
            message.setData(newMessage);
            message.setEditedAt(Instant.now());

            session.persist(message);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean deleteMessage(long messageId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            Message message = session.get(Message.class, messageId);
            message.setDeletedAt(Instant.now());

            session.persist(message);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public List<Message> getMessagesOfUser(String userName) {
        try (Session session = DbContext.getConnection()) {
            CriteriaQuery<User> userByUsernameQuery = UserRepo.getUserByUsernameQuery(session, userName);
            User user = session.createQuery(userByUsernameQuery).getSingleResult();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Message> criteria = cb.createQuery(Message.class);
            Root<Message> messageRoot = criteria.from(Message.class);
            criteria.select(messageRoot)
                    .where(cb.equal(messageRoot.get("sender"), user),
                            cb.isNull(messageRoot.get("deletedAt")));

            return session.createQuery(criteria).getResultList();
        } catch (Exception ignored) {
            return null;
        }
    }

    public long getNumberOfMessagesOfUser(String userName) {
        try (Session session = DbContext.getConnection()) {
            CriteriaQuery<User> userByUsernameQuery = UserRepo.getUserByUsernameQuery(session, userName);
            User user = session.createQuery(userByUsernameQuery).getSingleResult();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
            Root<Message> messageRoot = criteria.from(Message.class);
            criteria.select(cb.count(messageRoot))
                    .where(
                            cb.equal(messageRoot.get("sender"), user),
                            cb.isNull(messageRoot.get("deletedAt")));

            return session.createQuery(criteria).getSingleResult();
        } catch (Exception ignored) {
            return -1;
        }
    }

    public double getAvgNumberOfMessages() {
        try (Session session = DbContext.getConnection()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<Long> subQueryUserCount = cb.createQuery(Long.class);
            Root<User> userRoot = subQueryUserCount.from(User.class);
            subQueryUserCount.select(cb.count(userRoot))
                    .where(cb.isNull(userRoot.get("deletedAt")));
            long countUsers = session.createQuery(subQueryUserCount).getSingleResult();

            CriteriaQuery<Long> subQueryMessageCount = cb.createQuery(Long.class);
            Root<Message> messageRoot = subQueryMessageCount.from(Message.class);
            subQueryMessageCount.select(cb.count(messageRoot))
                    .where(cb.isNull(messageRoot.get("deletedAt")));
            long countMessages = session.createQuery(subQueryMessageCount).getSingleResult();

            return (double) countMessages / countUsers;
        } catch (Exception ignored) {
            return -1;
        }
    }

    public boolean readMessage(String userName, long messageId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            CriteriaQuery<User> userByUsernameQuery = UserRepo.getUserByUsernameQuery(session, userName);
            User user = session.createQuery(userByUsernameQuery).getSingleResult();

            Message message = session.get(Message.class, messageId);

            ReadMessage readMessage = ReadMessage.builder()
                    .message(message)
                    .user(user).build();

            session.persist(readMessage);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public long getNumberOfViewsOfMessage(long messageId) {
        try (Session session = DbContext.getConnection()) {
            Message message = session.get(Message.class, messageId);

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
            Root<ReadMessage> messageRoot = criteria.from(ReadMessage.class);
            criteria.select(cb.count(messageRoot))
                    .where(cb.equal(messageRoot.get("message"), message));

            return session.createQuery(criteria).getSingleResult();
        } catch (Exception ignored) {
            return -1;
        }
    }

    private CriteriaQuery<UserChat> getUserChatByUserAndChat(Session session, User user, Chat chat) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserChat> subQuery = cb.createQuery(UserChat.class);
        Root<UserChat> userChatRoot = subQuery.from(UserChat.class);
        subQuery.select(userChatRoot)
                .where(cb.equal(userChatRoot.get("user"), user),
                        cb.equal(userChatRoot.get("chat"), chat));

        return subQuery;
    }
}
