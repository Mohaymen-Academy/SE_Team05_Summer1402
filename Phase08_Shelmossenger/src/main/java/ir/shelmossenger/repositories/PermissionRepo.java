package ir.shelmossenger.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.shelmossenger.model.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import static ir.shelmossenger.context.DbContext.getConnection;

public class PermissionRepo {

    public boolean addPermissionToUserChat(Permission permission, long userChatId) {
        Session session = getConnection();
        session.beginTransaction();
        UserChatPermission userChatPermission=new UserChatPermission(){{
            setPermission(permission);
            setUserChat(new UserChat(){{setId(userChatId);}});
        }};
        session.persist(userChatPermission);
        session.getTransaction().commit();
        session.close();
        return true;
    }
}
