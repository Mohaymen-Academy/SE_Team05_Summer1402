package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.User;
import ir.shelmossenger.services.HashGenerator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
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
        try (Session session = DbContext.getConnection()) {
            String sql = """
                    WITH x_username AS (SELECT :userName)
                    SELECT COUNT(u.*) AS user_count
                    FROM users u
                             INNER JOIN user_chat uc ON u.id = uc.user_id
                             INNER JOIN chats c ON c.id = uc.chat_id
                    WHERE c.id IN (SELECT uxc.chat_id
                                   FROM users ux
                                            INNER JOIN user_chat uxc ON ux.id = uxc.user_id
                                   WHERE ux.user_name = (SELECT * FROM x_username))
                    AND c.deleted_at IS NULL
                    AND u.deleted_at IS NULL
                    AND u.user_name <> (SELECT * FROM x_username);""";

            NativeQuery<Long> nativeQuery = session.createNativeQuery(sql, Long.class);
            nativeQuery.setParameter("userName", userName);
            return nativeQuery.getSingleResult();
        } catch (Exception ignored) {
            return -1;
        }
    }

    static CriteriaQuery<User> getUserByUsernameQuery(Session session, String userName) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> subQuery = cb.createQuery(User.class);
        Root<User> userRoot = subQuery.from(User.class);
        subQuery.select(userRoot)
                .where(cb.equal(userRoot.get("username"), userName));

        return subQuery;
    }
}
