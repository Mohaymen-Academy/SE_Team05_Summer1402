package ir.shelmossenger.repositories;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.shelmossenger.model.Chat;
import ir.shelmossenger.model.ChatType;
import static ir.shelmossenger.context.DbContext.getConnection;

public class ChatRepo {
    public long addChat(Chat chat) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection()
                    .prepareStatement("insert into chats (title, link, chat_type, created_at, deleted_at)\r\n" + //
                            "values (?,?,?,?,?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, chat.getTitle());
        stmt.setString(2, chat.getLink());
        stmt.setLong(3, chat.getChatType().getId());
        stmt.setTimestamp(4, Timestamp.from(chat.getCreatedAt()));
        stmt.setTimestamp(5, Timestamp.from(chat.getDeletedAt()));
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!rs.next()) {
            return -1;
        }
        var keyRes = stmt.getGeneratedKeys();
        keyRes.next();
        return keyRes.getLong(1);
    }

    public boolean createPVChat(String userName1, String userName2) throws SQLException {
        var chatId = createChat(null, null, ChatType.PV);
        if (chatId == -1) {
            return false;
        }
        return addUserToChat(userName1, chatId) && addUserToChat(userName2, chatId);
    }

    public long createChat(String title, String link, ChatType chatType) throws SQLException {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setTitle(link);
        chat.setChatType(chatType);
        return addChat(chat);
    }

    public boolean addUserToChat(String userName, long chatId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection()
                    .prepareStatement(
                            "insert into user_chat (user_id, chat_id)\r\n" + //
                                    "values ((select id from users where user_name = ?), ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, userName);
        stmt.setLong(2, chatId);
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
