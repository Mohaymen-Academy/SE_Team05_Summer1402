package ir.shelmossenger;

import ir.shelmossenger.model.*;
import ir.shelmossenger.repositories.ChatRepo;
import ir.shelmossenger.repositories.MessageRepo;
import ir.shelmossenger.repositories.PermissionRepo;
import ir.shelmossenger.repositories.UserRepo;

public class Main {

    public static void main(String[] args) {
//        signup();
//        login();
//        deleteAccount();
//        changeBio();
//        addChat();
//        addUserToChat();
//        addPermissionToUserChat();
//        sendMessage();
        editMessage();
    }

    private static void signup() {
        User user = new User();
        user.setFullName("hamid");
        user.setUsername("hamid2");
        user.setPassword("h1245");
        user.setPhoneNumber("09123456789");
        user.setEmail("hamid@gmail.com");
//        user.setBio("sth");

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.signup(user));
    }

    private static void login() {
        String username = "amir123";
        String password = "a123";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.login(username, password));
    }

    private static void deleteAccount() {
        String username = "hamed1";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.deleteAccount(username));
    }

    private static void changeBio() {
        String username = "sara2";
        String bio = "bfs";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.changeBio(username, bio));
    }

    private static void addChat() {
        Chat chat = new Chat();
        chat.setTitle("gpTest");
        chat.setChatType(ChatType.GROUP);
        chat.setLink("test_chat");

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.addChat(chat));
    }

    private static void addUserToChat() {
        String username = "sara2";
        long chatId = 3;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.addUserToChat(username, chatId));
    }

    private static void addPermissionToUserChat() {
        Permission permission = Permission.MESSAGE;
        long userChatId = 2;

        PermissionRepo permissionRepo = new PermissionRepo();
        System.out.println(permissionRepo.addPermissionToUserChat(permission, userChatId));
    }

    private static void sendMessage() {
        String data = "hello";
        MessageType messageType = MessageType.MESSAGE;
        String senderUsername = "hamid2";
        long chatId = 5;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.sendMessage(data, messageType, senderUsername, chatId));
    }

    private static void editMessage(){
        String newMessage = "hi";
        long messageId = 1;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.editMessage(newMessage, messageId));
    }
}
