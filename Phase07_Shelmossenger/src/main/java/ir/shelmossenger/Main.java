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
//        createChat();
//        addUserToChat();
//        sendMessage();
//        editMessage();
//        deleteMessage();
//        averageNumberOfMessages();
//        getNumberOfMessagesOfUSer();
//        readMessage();
//        getNumberOfViewsOfMessage();
//        getMessagesOfUser();
//        addPermissionToUserChat();
        getNumberOfRelationshipsOfUser();
    }

    private static void signup() {
        UserRepo userRepo = new UserRepo();
        User user = new User();
//        user.setId(100L);
        user.setFullName("hamid");
        user.setUserName("hamid");
        user.setPassword("h123");
        user.setEmail("hamid@gmail.com");
        user.setPhoneNumber("1319123457780");
//        user.setBio("one thing");

        System.out.println(userRepo.signup(user));
    }

    private static void login() {
        String username = "karen";
        String password = "k123";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.login(username, password));
    }

    private static void deleteAccount() {
        String username = "ham";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.deleteAccount(username));
    }

    private static void changeBio() {
        String username = "hamid";
        String bio = "do";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.changeBio(username, bio));
    }

    private static void createChat() {
        String title = "pv_test";
        String link = "pvt";
        ChatType chatType = ChatType.PV;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.createChat(title, link, chatType));
    }

    private static void addUserToChat() {
        String username = "hamid";
        long chatID = 3;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.addUserToChat(username, chatID));
    }

    private static void sendMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE);
        message.setData("hello");
        message.setSenderId(5L);
        message.setChatId(3L);

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.sendMessage(message));
    }

    private static void editMessage() {
        String newMessage = "hi";
        long messageId = 6;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.editMessage(newMessage, messageId));
    }

    private static void deleteMessage() {
        long messageId = 7;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.deleteMessage(messageId));
    }

    private static void averageNumberOfMessages() {
        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getAvgNumberOfMessages());
    }

    private static void getNumberOfMessagesOfUSer() {
        String username = "sara";

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getNumberOfMessagesOfUser(username));
    }

    private static void readMessage() {
        String username = "karen";
        long messageId = 6;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.readMessage(username, messageId));
    }

    private static void getNumberOfViewsOfMessage() {
        long messageId = 6;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getNumberOfViewsOfMessage(messageId));
    }

    private static void getMessagesOfUser() {
        String username = "sara";

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getMessagesOfUser(username));
    }

    private static void addPermissionToUserChat() {
        Permission permission = Permission.ADMIN;
        long userChatId = 10;

        PermissionRepo permissionRepo = new PermissionRepo();
        System.out.println(permissionRepo.addPermissionToUserChat(permission, userChatId));
    }

    private static void getNumberOfRelationshipsOfUser() {
        String username = "sara";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.getNumberOfRelationshipsOfUser(username));
    }
}
