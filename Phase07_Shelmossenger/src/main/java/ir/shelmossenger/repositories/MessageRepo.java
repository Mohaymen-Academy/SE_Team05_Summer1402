package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.Message;
import ir.shelmossenger.model.MessageType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageRepo {

    public boolean sendMessage(Message message) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            INSERT INTO messages (data, message_type, sender_id, chat_id)
                            VALUES (?, (SELECT id FROM message_types WHERE type_name = ?), ?,
                                       (SELECT chat_id FROM user_chat WHERE user_id = ? AND chat_id = ?));""")) {

                stmt.setString(1, message.getData());
                stmt.setString(2, message.getMessageType().getTypeName());
                stmt.setLong(3, message.getSenderId());
                stmt.setLong(4, message.getSenderId());
                stmt.setLong(5, message.getChatId());

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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            UPDATE messages
                            SET (data, edited_at) = (?, CURRENT_TIMESTAMP)
                            WHERE id = ?
                              AND deleted_at IS NULL;""")) {

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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            UPDATE messages
                            SET deleted_at = CURRENT_TIMESTAMP
                            WHERE id = ?
                              AND deleted_at IS NULL;""")) {

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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            SELECT m.*
                            FROM messages m
                            WHERE m.sender_id = (SELECT id FROM users WHERE user_name = ?)
                              AND m.deleted_at IS NULL;""")) {

                stmt.setString(1, userName);

                try (ResultSet rs = stmt.executeQuery()) {
                    List<Message> messages = new ArrayList<>();
                    while (rs.next()) {
                        Message message = new Message();
                        message.setId(rs.getLong("id"));
                        message.setData(rs.getString("data"));
                        message.setMessageType(MessageType.getById((int) rs.getLong("message_type") - 1));
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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            SELECT COUNT(m.id)
                            FROM messages m
                            WHERE m.sender_id = (SELECT id FROM users WHERE user_name = ?)
                              AND m.deleted_at IS NULL;""")) {

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
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            SELECT COUNT(m.id)::decimal / (SELECT COUNT(id) FROM users WHERE deleted_at IS NULL)
                            FROM messages m
                            WHERE m.deleted_at IS NULL;""")) {

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

    public boolean readMessage(String userName, long messageId) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            INSERT INTO read_message (user_id, message_id)
                            VALUES ((SELECT id FROM users WHERE user_name=?), ?);""")) {

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

    public long getNumberOfViewsOfMessage(long messageId) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            SELECT COUNT(*)
                            FROM read_message
                            WHERE message_id = ?;""")) {

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
}
