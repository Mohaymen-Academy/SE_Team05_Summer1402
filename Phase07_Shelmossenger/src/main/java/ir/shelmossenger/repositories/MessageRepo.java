package ir.shelmossenger.repositories;

import ir.shelmossenger.model.Message;
import ir.shelmossenger.model.MessageType;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static ir.shelmossenger.context.DbContext.getConnection;

public class MessageRepo {

    // TODO: 8/6/2023 add messages only if user is in chat
    public boolean sendMessage(Message message) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            insert into messages (data, message_type, sender_id, chat_id)\r
                            values (?, (Select id from message_types where type_name = ?), ?, ?);""")) {

                stmt.setString(1, message.getData());
                stmt.setString(2, message.getMessageType().getTypeName());
                stmt.setLong(3, message.getSenderId());
                stmt.setLong(4, message.getChatId());

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

    public boolean editMessage(String newMessage, long messageId) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                         """
                                 update messages\r
                                 set (data, edited_at) = (?, current_timestamp)\r
                                 where id=?;""")) {

                stmt.setString(1, newMessage);
                stmt.setLong(2, messageId);

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

    public boolean readMessage(String userName, long messageId)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "insert into read_message (user_id, message_id)\r\n" + //
                            "values ((select id from users where user_name=?),?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, userName);
        stmt.setLong(2, messageId);
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
