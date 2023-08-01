package ir.shelmossenger.repositories;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.shelmossenger.model.Chat;
import ir.shelmossenger.model.ChatType;
import ir.shelmossenger.model.Permission;

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
        if (!rs.rowInserted()) {
            return -1;
        }
        var keyRes = stmt.getGeneratedKeys();
        keyRes.next();
        return keyRes.getLong(1);
    }

    public long createPVChat(String userName1, String userName2) throws SQLException {
        var chatId = createChat(null, null, ChatType.PV);
        PermissionRepo permissionRepo = new PermissionRepo();
        var userChatId1 = addUserToChat(userName1, chatId);
        Permission[] permissions = new Permission[] {
                Permission.MESSAGE,
                Permission.IMAGE,
                Permission.VIDEO,
                Permission.VOICE,
                Permission.FILE,
        };
        for (Permission permission : permissions) {
            permissionRepo.addPermissionToUserChat(permission, userChatId1);
        }
        var userChatId2 = addUserToChat(userName2, chatId);
        for (Permission permission : permissions) {
            permissionRepo.addPermissionToUserChat(permission, userChatId2);
        }
        return chatId;
    }

    public long createChat(String title, String link, ChatType chatType) throws SQLException {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setTitle(link);
        chat.setChatType(chatType);
        return addChat(chat);
    }

    public long addUserToChat(String userName, long chatId) throws SQLException {
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
        if (!rs.rowInserted()) {
            return -1;
        }
        var keyRes = stmt.getGeneratedKeys();
        keyRes.next();
        return keyRes.getLong(1);
    }

}
