package ir.shelmossenger.repositories;

import ir.shelmossenger.model.Chat;
import ir.shelmossenger.model.ChatType;
import ir.shelmossenger.model.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ir.shelmossenger.context.DbContext.getConnection;

public class ChatRepo {

    public long createChat(String title, String link, ChatType chatType) {
        Chat chat = new Chat();
        chat.setTitle(title);
        chat.setLink(link);
        chat.setChatType(chatType);
        return addChat(chat);
    }

    public long addChat(Chat chat) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            insert into chats (title, link, chat_type)\r
                            values (?,?,?);""")) {

                stmt.setString(1, chat.getTitle());
                stmt.setString(2, chat.getLink());
                stmt.setLong(3, chat.getChatType().getId());

                int numberOfAddedRows = stmt.executeUpdate();

                // TODO: 8/6/2023 is not returning id
                if (numberOfAddedRows == 0) return -1;

                try (ResultSet keyRes = stmt.getGeneratedKeys()) {
                    if (keyRes.next()) return keyRes.getLong(1);
                    else return -1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: 8/6/2023
    public long createPVChat(String userName1, String userName2) {
        long chatId = createChat(null, null, ChatType.PV);

        PermissionRepo permissionRepo = new PermissionRepo();
        Permission[] permissions = new Permission[]{
                Permission.MESSAGE,
                Permission.IMAGE,
                Permission.VIDEO,
                Permission.VOICE,
                Permission.FILE,
        };

        long userChatId1 = addUserToChat(userName1, chatId);
        for (Permission permission : permissions)
            permissionRepo.addPermissionToUserChat(permission, userChatId1);
        long userChatId2 = addUserToChat(userName2, chatId);
        for (Permission permission : permissions)
            permissionRepo.addPermissionToUserChat(permission, userChatId2);
        return chatId;
    }

    public long addUserToChat(String userName, long chatId) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement stmt = connection.prepareStatement(
                    """
                            insert into user_chat (user_id, chat_id)\r
                            values ((select id from users where user_name = ?), ?);""")) {

                stmt.setString(1, userName);
                stmt.setLong(2, chatId);

                int numberOfAddedRows;
                try {
                    numberOfAddedRows = stmt.executeUpdate();
                } catch (Exception ignored) {
                    return -1;
                }

                // TODO: 8/6/2023 is not returning id
                if (numberOfAddedRows == 0) return -1;
                try (ResultSet keyRes = stmt.getGeneratedKeys()) {
                    if (keyRes.next()) return keyRes.getLong(1);
                    else return -1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
