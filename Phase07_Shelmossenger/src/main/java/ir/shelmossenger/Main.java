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
//        getNumberOfRelationshipsOfUser();
//        createChat();
//        addUserToChat();
//        addPermissionToUserChat();
//        sendMessage();
//        editMessage();
        deleteMessage();
//        getMessagesOfUser();
//        getNumberOfMessagesOfUSer();
//        averageNumberOfMessages();
//        readMessage();
//        getNumberOfViewsOfMessage();
    }

    private static void signup() {
        UserRepo userRepo = new UserRepo();
        User user = new User();
        user.setFullName("hami");
        user.setUserName("hami");
        user.setPassword("h123");
        user.setEmail("hami@gmail.com");
        user.setPhoneNumber("01319123457780");
        user.setBio("one thing");

        System.out.println(userRepo.signup(user));
    }

    private static void login() {
        String username = "hamid";
        String password = "h123";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.login(username, password));
    }

    private static void deleteAccount() {
        String username = "ali698";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.deleteAccount(username));
    }

    private static void changeBio() {
        String username = "hamid";
        String bio = "do";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.changeBio(username, bio));
    }

    private static void getNumberOfRelationshipsOfUser() {
        String username = "hamid";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.getNumberOfRelationshipsOfUser(username));
    }

    private static void createChat() {
        String title = "channel_test";
        String link = "chnt";
        ChatType chatType = ChatType.CHANNEL;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.createChat(title, link, chatType));
    }

    private static void addUserToChat() {
        String username = "sara";
        long chatID = 1;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.addUserToChat(username, chatID));
    }

    private static void addPermissionToUserChat() {
        Permission permission = Permission.ADMIN;
        long userId = 3;
        long chatId = 1;

        PermissionRepo permissionRepo = new PermissionRepo();
        System.out.println(permissionRepo.addPermissionToUserChat(permission, userId, chatId));
    }

    private static void sendMessage() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE);
        message.setData("hello");
        message.setSenderId(3L);
        message.setChatId(1L);

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.sendMessage(message));
    }

    private static void editMessage() {
        String newMessage = "hi";
        long messageId = 2;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.editMessage(newMessage, messageId));
    }

    private static void deleteMessage() {
        long messageId = 5;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.deleteMessage(messageId));
    }

    private static void getMessagesOfUser() {
        String username = "hamid";

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getMessagesOfUser(username));
    }

    private static void getNumberOfMessagesOfUSer() {
        String username = "hamid";

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getNumberOfMessagesOfUser(username));
    }

    private static void averageNumberOfMessages() {
        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getAvgNumberOfMessages());
    }

    private static void readMessage() {
        String username = "hami";
        long messageId = 5;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.readMessage(username, messageId));
    }

    private static void getNumberOfViewsOfMessage() {
        long messageId = 5;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.getNumberOfViewsOfMessage(messageId));
    }
}
