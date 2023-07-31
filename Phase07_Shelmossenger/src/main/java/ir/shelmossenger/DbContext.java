package ir.shelmossenger;

import ir.shelmossenger.model.User;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class DbContext {
    private DataSource dataSource;

    public DbContext() {
        dataSource = createDataSource();
    }

    private Connection getConnection() {
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private DataSource createDataSource() {
        // The url specifies the address of our database along with username and
        // password credentials
        // you should replace these with your own username and password
        String dbName = "shelmossenger";
        String userName = "postgres";
        String password = "1234";
        String url = MessageFormat.format("jdbc:postgresql://localhost:5432/{0}?user={1}&password={2}", dbName,
                userName, password);
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    public boolean signup(User user) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "insert into users (full_name, user_name,password, email, phone_number, bio)\n" +
                            "values (?, ?,?, ?, ?, ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, user.getFullName());
        stmt.setString(2, user.getUserName());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getEmail());
        stmt.setString(5, user.getPhoneNumber());
        stmt.setString(6, user.getBio());
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs.rowInserted() || rs.rowUpdated();
    }

    public boolean login(String userName, String password) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "select user_name,password\r\n" + //
                            "from users\r\n" + //
                            "where user_name=?;");
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
            return false;
        }
        return rs.getString("password").equals(password);
    }

    public boolean deleteAccount(String userName) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "update users\r\n" + //
                            "set deleted_at=current_timestamp\r\n" + //
                            "where user_name = ?;");
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
            return false;
        }
        return rs.rowDeleted();
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

    public boolean sendMessage(String message, String messageType, String senderUserName, long chatId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "insert into messages (data, message_type, sender_id, chat_id)\r\n" + //
                            "values (?, (Select id from message_types where type_name = ?),\r\n" + //
                            "        (select id from users where user_name = ?), ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, message);
        stmt.setString(2, message);
        stmt.setString(3, senderUserName);
        stmt.setLong(4, chatId);
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
        return rs.rowInserted();
    }

    public boolean editMessage(String newMessage, long messageId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "update messages\r\n" + //
                            "set data = ?\r\n" + //
                            "where id=?;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, newMessage);
        stmt.setLong(2, messageId);
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

    public boolean deleteMessage(long messageId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "delete\r\n" + //
                            "from messages\r\n" + //
                            "where id=?;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setLong(1, messageId);
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
        return rs.rowDeleted();
    }
}
