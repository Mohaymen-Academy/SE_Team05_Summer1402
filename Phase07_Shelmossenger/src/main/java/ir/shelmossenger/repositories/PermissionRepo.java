package ir.shelmossenger.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import ir.shelmossenger.model.Permission;

import static ir.shelmossenger.context.DbContext.getConnection;

public class PermissionRepo {

    public boolean addPermissionToUserChat(Permission permission, long userChatId) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            insert into user_chat_permission (user_chat_id, permission_id)\r
                            values (?, ?);""")) {

                stmt.setLong(1, userChatId);
                stmt.setLong(2, permission.getId());

                int numberOfAddedRows;
                try {
                    numberOfAddedRows = stmt.executeUpdate();
                    return numberOfAddedRows > 0;
                } catch (Exception ignored) {
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
