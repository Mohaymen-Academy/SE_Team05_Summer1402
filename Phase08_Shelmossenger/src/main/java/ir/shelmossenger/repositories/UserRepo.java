package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.User;
import ir.shelmossenger.services.HashGenerator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.Instant;

public class UserRepo {

    public boolean signup(User user) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();
            user.setPassword(HashGenerator.Hash(user.getPassword()));
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean login(String userName, String password) {
        try (Session session = DbContext.getConnection()) {
            CriteriaQuery<User> userByUsernameQuery = getUserByUsernameQuery(session, userName);

            User user = session.createQuery(userByUsernameQuery).getSingleResult();
            return user.getPassword().equals(HashGenerator.Hash(password));
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean deleteAccount(String userName) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();
            CriteriaQuery<User> userByUsernameQuery = getUserByUsernameQuery(session, userName);

            User user = session.createQuery(userByUsernameQuery).getSingleResult();
            user.setDeletedAt(Instant.now());
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean changeBio(String userName, String bio) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();
            CriteriaQuery<User> userByUsernameQuery = getUserByUsernameQuery(session, userName);

            User user = session.createQuery(userByUsernameQuery).getSingleResult();
            user.setBio(bio);
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public long getNumberOfRelationshipsOfUser(String userName) {
        Session session = DbContext.getConnection();
        session.beginTransaction();
        String sql = "with x_username as (select :userName)\r\n" + //
                "select count(u.*) as user_count\r\n" + //
                "from users u\r\n" + //
                "         inner join user_chat uc on u.id = uc.user_id\r\n" + //
                "         inner join chats c on c.id = uc.chat_id\r\n" + //
                "where c.id in (select uxc.chat_id\r\n" + //
                "               from users ux\r\n" + //
                "                        inner join user_chat uxc on ux.id = uxc.user_id\r\n" + //
                "               where ux.user_name = (select * from x_username)\r\n" + //
                ")  and c.deleted_at is null\r\n" + //
                "  and u.deleted_at is null\r\n" + //
                "  and u.user_name <>  (select * from x_username);";
        var query = session.createNativeQuery(sql);
        query.setParameter("userName", userName);
        long count = (long) query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return count;
    }

    private CriteriaQuery<User> getUserByUsernameQuery(Session session, String userName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> subQuery = cb.createQuery(User.class);
        Root<User> userRoot = subQuery.from(User.class);
        subQuery.select(userRoot)
                .where(cb.equal(userRoot.get("username"), userName));

        return subQuery;
    }
}
