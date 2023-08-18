package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.Permission;
import ir.shelmossenger.model.UserChat;
import ir.shelmossenger.model.UserChatPermission;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PermissionRepo {

    public boolean addPermissionToUserChat(Permission permission, long userChatId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            UserChatPermission userChatPermission = new UserChatPermission();
            userChatPermission.setPermission(permission);
            userChatPermission.setUserChat(new UserChat() {{
                setId(userChatId);
            }});

            session.persist(userChatPermission);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
