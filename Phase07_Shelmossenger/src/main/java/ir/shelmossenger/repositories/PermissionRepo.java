package ir.shelmossenger.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.shelmossenger.model.Permission;

import static ir.shelmossenger.context.DbContext.getConnection;

public class PermissionRepo {

    public boolean addPermissionToUserChat(Permission permission, long userChatId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection()
                    .prepareStatement(
                            "insert into user_chat_permission (user_chat_id, permission_id)\r\n" + //
                                    "values (?, ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setLong(1, userChatId);
        stmt.setLong(2, permission.getId());
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs.rowInserted();
    }
}
