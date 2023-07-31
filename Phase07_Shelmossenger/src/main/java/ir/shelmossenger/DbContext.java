package ir.shelmossenger;

import ir.shelmossenger.model.Message;
import ir.shelmossenger.model.MessageType;
import ir.shelmossenger.model.User;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    public List<Message> getMessagesOfUser(String userName)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "select m.*\r\n" + //
                            "from messages m\r\n" + //
                            "where m.sender_id = (select id from users where user_name = ?)\r\n" + //
                            "  and m.deleted_at is null;");
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
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            Message message = new Message();
            message.setId(rs.getLong("id"));
            message.setData(rs.getString("data"));
            message.setMessageType(MessageType.getById((int) rs.getLong("message_type")));
            message.setSentAt(rs.getTimestamp("sent_at").toInstant());
            message.setEditedAt(rs.getTimestamp("edited_at").toInstant());
            message.setDeletedAt(rs.getTimestamp("deleted_at").toInstant());
            message.setSenderId(rs.getLong("sender_id"));
            message.setChatId(rs.getLong("chat_id"));
            message.setReplyId(rs.getLong("reply_id"));
            messages.add(message);
        }
        return messages;
    }

    public long getNumberOfMessagesOfUser(String userName)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "select count(m.id)\r\n" + //
                            "from messages m\r\n" + //
                            "where m.sender_id = (select id from users where user_name = ?)\r\n" + //
                            "  and m.deleted_at is null;");
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

    public double getAvgNumberOfMessages()
            throws SQLException {
        Statement stmt = null;
        String query = "select count(m.id)::decimal / (select count(id) from users where deleted_at is null)\n" +
                "from messages m\n" +
                "where m.deleted_at is null;";
        try {
            stmt = getConnection().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!rs.next()) {
            return 0;
        }
        return rs.getDouble(0);
    }

    public long getNumberOfViewsOfMessage(long messageId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "select count(*)\r\n" + //
                            "from read_message\r\n" + //
                            "where message_id=?;");
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
            return 0;
        }
        return rs.getLong(0);
    }

}
