package ir.shelmossenger.repositories;

import ir.shelmossenger.model.User;
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

    public boolean changeBio(String userName, String bio) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            update users\r
                            set bio=?\r
                            where user_name = ?;""")) {

                stmt.setString(1, bio);
                stmt.setString(2, userName);

                int numberOfChangedRows = stmt.executeUpdate();
                return numberOfChangedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getNumberOfRelationshipsOfUser(String userName) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            with x_username as (select ?)\r
                            select count(u.*) as user_count\r
                            from users u\r
                                     inner join user_chat uc on u.id = uc.user_id\r
                                     inner join chats c on c.id = uc.chat_id\r
                            where c.id in (select uxc.chat_id\r
                                           from users ux\r
                                                    inner join user_chat uxc on ux.id = uxc.user_id\r
                                           where ux.user_name = (select * from x_username)\r
                            )  and c.deleted_at is null\r
                              and u.deleted_at is null\r
                              and u.user_name <>  (select * from x_username);""")) {

                stmt.setString(1, userName);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) return 0;
                    return rs.getLong(1);
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
}
