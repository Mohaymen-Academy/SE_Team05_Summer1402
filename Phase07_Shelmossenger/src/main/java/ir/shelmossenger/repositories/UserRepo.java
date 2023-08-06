package ir.shelmossenger.repositories;

import ir.shelmossenger.model.User;
import ir.shelmossenger.services.HashGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ir.shelmossenger.context.DbContext.getConnection;

public class UserRepo {

    public boolean signup(User user) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            insert into users (full_name, user_name,password, email, phone_number, bio)\r
                            values (?, ?,?, ?, ?, ?)""")) {

                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getUserName());
//                stmt.setString(3, HashGenerator.Hash(user.getPassword()));
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getPhoneNumber());
                stmt.setString(6, user.getBio());

                int numberOfAddedRows = stmt.executeUpdate();
                // Execute the query, and store the number of changed rows
                return numberOfAddedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String userName, String password) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """ 
                            select user_name,password\r
                            from users\r
                            where user_name=?;""")) {

                stmt.setString(1, userName);
                try (ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) return false;
//                    return rs.getString("password").equals(HashGenerator.Hash(password));
                    return rs.getString("password").equals(password);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteAccount(String userName) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            update users\r
                            set deleted_at=current_timestamp\r
                            where user_name = ?;""")) {

                stmt.setString(1, userName);
                int numberOfDeletedRows = stmt.executeUpdate();
                return numberOfDeletedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changeBio(String userName, String bio) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "update users\r\n" + //
                            "set bio=?\r\n" + //
                            "where user_name = ?;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, bio);
        stmt.setString(2, userName);
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!rs.next()) {
            return false;
        }
        return rs.rowUpdated();
    }

    public long getNumberOfRelationshipsOfUser(String userName)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "with x_username as (select ?)\r\n" + //
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
                            "  and u.user_name <>  (select * from x_username);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, userName);
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!rs.next()) {
            return 0;
        }
        return rs.getLong(0);
    }

}
