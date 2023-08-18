package ir.shelmossenger.repositories;

import ir.shelmossenger.model.Message;
import ir.shelmossenger.model.MessageType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ir.shelmossenger.context.DbContext.getConnection;

public class MessageRepo {

    // TODO: 8/6/2023 add messages only if user is in chat
    // TODO: 8/6/2023 check permission before sending message
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

    public boolean deleteMessage(long messageId) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            update messages\r
                            set deleted_at=current_timestamp\r
                            where id=?;""")) {

                stmt.setLong(1, messageId);

                int numberOfAddedRows = stmt.executeUpdate();
                return numberOfAddedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> getMessagesOfUser(String userName) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            select m.*\r
                            from messages m\r
                            where m.sender_id = (select id from users where user_name = ?)\r
                              and m.deleted_at is null;""")) {

                stmt.setString(1, userName);

                try (ResultSet rs = stmt.executeQuery()) {
                    List<Message> messages = new ArrayList<>();
                    while (rs.next()) {
                        Message message = new Message();
                        message.setId(rs.getLong("id"));
                        message.setData(rs.getString("data"));
                        message.setMessageType(MessageType.getById((int) rs.getLong("message_type") - 1));
                        if (rs.getTimestamp("sent_at") != null)
                            message.setSentAt(rs.getTimestamp("sent_at").toInstant());
                        if (rs.getTimestamp("edited_at") != null)
                            message.setEditedAt(rs.getTimestamp("edited_at").toInstant());
                        message.setDeletedAt(null);
                        message.setSenderId(rs.getLong("sender_id"));
                        message.setChatId(rs.getLong("chat_id"));
                        message.setReplyId(rs.getLong("reply_id"));
                        messages.add(message);
                    }
                    return messages;
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

    public long getNumberOfMessagesOfUser(String userName) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            select count(m.id)\r
                            from messages m\r
                            where m.sender_id = (select id from users where user_name = ?)\r
                              and m.deleted_at is null;""")) {

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

    public double getAvgNumberOfMessages() {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            select count(m.id)::decimal / (select count(id) from users where deleted_at is null)\r
                            from messages m\r
                            where m.deleted_at is null;""")) {

                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) return 0;
                    return rs.getDouble(1);
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

    public long getNumberOfViewsOfMessage(long messageId) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            select count(*)\r
                            from read_message\r
                            where message_id=?;""")) {

                stmt.setLong(1, messageId);

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

    public boolean readMessage(String userName, long messageId) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            insert into read_message (user_id, message_id)\r
                            values ((select id from users where user_name=?),?);""")) {

                stmt.setString(1, userName);
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
}
