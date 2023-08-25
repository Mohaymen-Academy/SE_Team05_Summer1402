package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.Permission;
import ir.shelmossenger.model.UserChat;
import ir.shelmossenger.model.UserChatPermission;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PermissionRepo {

    private static PermissionRepo permissionRepo;

    private PermissionRepo() {
    }

    public static PermissionRepo getInstance() {
        if (permissionRepo == null) permissionRepo = new PermissionRepo();
        return permissionRepo;
    }

    public boolean addPermissionToUserChat(Permission permission, long userChatId) {
        try (Session session = DbContext.getConnection()) {
            Transaction transaction = session.beginTransaction();

            UserChatPermission userChatPermission = UserChatPermission.builder()
                    .permission(permission)
                    .userChat(UserChat.builder().id(userChatId).build()).build();

            session.persist(userChatPermission);
            transaction.commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
