package ir.shelmossenger.repositories;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.Chat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatRepo {

    public boolean addChat(Chat chat) {
        try (Connection connection = DbContext.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            INSERT INTO chats (title, link, chat_type)
                            VALUES (?, ?, (SELECT id FROM chat_types WHERE type_name = ?));""")) {

                stmt.setString(1, chat.getTitle());
                stmt.setString(2, chat.getLink());
                stmt.setString(3, chat.getChatType().getTypeName());

                int numberOfAddedRows = stmt.executeUpdate();

                return numberOfAddedRows > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addUserToChat(String userName, long chatId) {
        try (Connection connection = DbContext.getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            INSERT INTO user_chat (user_id, chat_id)
                            VALUES ((SELECT id FROM users WHERE user_name = ?), ?);""")) {

                stmt.setString(1, userName);
                stmt.setLong(2, chatId);

                int numberOfAddedRows;
                try {
                    numberOfAddedRows = stmt.executeUpdate();
                    return numberOfAddedRows > 0;
                } catch (SQLException ignored) {
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
