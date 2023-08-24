package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.User;
import ir.shelmossenger.services.HashGenerator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepo {

    private static UserRepo userRepo;

    private UserRepo() {
    }

    public static UserRepo getInstance() {
        if (userRepo == null) userRepo = new UserRepo();
        return userRepo;
    }

    public boolean signup(User user) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            INSERT INTO users (full_name, user_name,password, email, phone_number, bio)
                            VALUES (?, ?, ?, ?, ?, ?)""")) {

                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getUserName());
                stmt.setString(3, HashGenerator.Hash(user.getPassword()));
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getPhoneNumber());
                stmt.setString(6, user.getBio());

                int numberOfAddedRows = stmt.executeUpdate();
                return numberOfAddedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String userName, String password) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """ 
                            SELECT (user_name, password)
                            FROM users
                            WHERE user_name = ?;""")) {

                stmt.setString(1, userName);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) return false;
                    return rs.getString("password").equals(HashGenerator.Hash(password));
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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            UPDATE users
                            SET deleted_at = CURRENT_TIMESTAMP
                            WHERE user_name = ?;""")) {

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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            UPDATE users
                            SET bio = ?
                            WHERE user_name = ?;""")) {

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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            WITH x_username AS (SELECT ?)
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
                            AND u.user_name <> (SELECT * FROM x_username);""")) {

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
