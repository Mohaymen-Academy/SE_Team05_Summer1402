package ir.shelmossenger.repositories;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.*;
import org.hibernate.Session;

import static ir.shelmossenger.context.DbContext.getConnection;

public class ChatRepo {
    public long addChat(Chat chat) throws SQLException {
        Session session = getConnection();
        session.beginTransaction();
        session.persist(chat);
        session.getTransaction().commit();
        session.close();
        return chat.getId();
    }

    public long createPVChat(String userName1, String userName2) throws SQLException {
        var chatId = createChat(null, null, ChatType.PV);
        PermissionRepo permissionRepo = new PermissionRepo();
        var userChatId1 = addUserToChat(userName1, chatId);
        Permission[] permissions = new Permission[]{
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
        Session session = getConnection();
        session.beginTransaction();
        User user = new User() {{
            setUsername(userName);
        }};
        Chat chat = new Chat() {{
            setId(chatId);
        }};
        UserChat userChat = new UserChat() {
            {
                setUser(user);
                setChat(chat);
            }
        };
        session.persist(userChat);
        session.getTransaction().commit();
        session.close();
        return userChat.getId();
    }

}
