package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PermissionRepo {

    private static PermissionRepo permissionRepo;

    private PermissionRepo() {
    }

    public static PermissionRepo getInstance() {
        if (permissionRepo == null) permissionRepo = new PermissionRepo();
        return permissionRepo;
    }

    public boolean addPermissionToUserChat(Permission permission, long userId, long chatId) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            INSERT INTO user_chat_permission (user_chat_id, permission_id)
                            VALUES ((SELECT id FROM user_chat WHERE user_id = ? AND chat_id = ?),
                                    (SELECT id FROM permissions WHERE title = ?));""")) {

                stmt.setLong(1, userId);
                stmt.setLong(2, chatId);
                stmt.setString(3, permission.getTitle());

                try {
                    int numberOfAddedRows = stmt.executeUpdate();
                    return numberOfAddedRows > 0;
                } catch (SQLException ignored) {
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
