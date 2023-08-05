package ir.shelmossenger.repositories;

import ir.shelmossenger.model.*;
import ir.shelmossenger.services.HashGenerator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

import static ir.shelmossenger.context.DbContext.getConnection;

public class UserRepo {

    public boolean signup(User user) {
        Session session = getConnection();
        session.beginTransaction();
        user.setPassword(HashGenerator.Hash(user.getPassword()));
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean login(String userName, String password) throws SQLException {
        Session session = getConnection();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> subquery = cb.createQuery(User.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(userRoot)
                .where(cb.equal(userRoot.get("username"), userName));
        User user = session.createQuery(subquery).getSingleResult();
        session.close();

        if (user == null) {
            return false;
        }
        return user.getPassword().equals(HashGenerator.Hash(password));
    }

    public boolean deleteAccount(String userName) throws SQLException {
        Session session = getConnection();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> subquery = cb.createQuery(User.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(userRoot)
                .where(cb.equal(userRoot.get("username"), userName));
        User user = session.createQuery(subquery).getSingleResult();
        user.setDeletedAt(Instant.now());
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean changeBio(String userName, String bio) throws SQLException {
        Session session = getConnection();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> subquery = cb.createQuery(User.class);
        Root<User> userRoot = subquery.from(User.class);
        subquery.select(userRoot)
                .where(cb.equal(userRoot.get("username"), userName));
        User user = session.createQuery(subquery).getSingleResult();
        user.setBio(bio);
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public long getNumberOfRelationshipsOfUser(String userName)
            throws SQLException {
        Session session = getConnection();
        session.beginTransaction();
        String sql="with x_username as (select :userName)\r\n" + //
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
        var query=session.createNativeQuery(sql);
        query.setParameter("userName",userName);
        long count= (long) query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return count;
    }

}
