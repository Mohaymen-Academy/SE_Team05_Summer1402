package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.Chat;
import ir.shelmossenger.model.User;
import ir.shelmossenger.model.UserChat;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ChatRepo {

    private static ChatRepo chatRepo;

    private ChatRepo() {
    }

    public static ChatRepo getInstance() {
        if (chatRepo == null) chatRepo = new ChatRepo();
        return chatRepo;
    }

    public long addChat(Chat chat) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();
            session.persist(chat);
            transaction.commit();
            return chat.getId();
        } catch (Exception ignored) {
            return -1;
        }
    }

    public long addUserToChat(String userName, long chatId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            CriteriaQuery<User> userByUsernameQuery = UserRepo.getUserByUsernameQuery(session, userName);
            User user = session.createQuery(userByUsernameQuery).getSingleResult();

            Chat chat = Chat.builder()
                    .id(chatId).build();

            UserChat userChat = UserChat.builder()
                    .user(user)
                    .chat(chat).build();

            session.persist(userChat);
            transaction.commit();
            return userChat.getId();
        } catch (Exception ignored) {
            return -1;
        }
    }
}
