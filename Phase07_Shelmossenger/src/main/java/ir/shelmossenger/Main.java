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
//        deleteMessage();
//        getMessagesOfUser();
//        getNumberOfMessagesOfUSer();
//        averageNumberOfMessages();
//        readMessage();
//        getNumberOfViewsOfMessage();
    }

    private static void signup() {
        User user = User.builder()
                .fullName("hami")
                .userName("hami")
                .password("h123")
                .email("hami@gmail.com")
                .phoneNumber("01319123457780")
                .bio("one thing").build();

        System.out.println(UserRepo.getInstance().signup(user));
    }

    private static void login() {
        String username = "hamid";
        String password = "h123";

        System.out.println(UserRepo.getInstance().login(username, password));
    }

    private static void deleteAccount() {
        String username = "ali698";

        System.out.println(UserRepo.getInstance().deleteAccount(username));
    }

    private static void changeBio() {
        String username = "hamid";
        String bio = "do";

        System.out.println(UserRepo.getInstance().changeBio(username, bio));
    }

    private static void getNumberOfRelationshipsOfUser() {
        String username = "hamid";

        System.out.println(UserRepo.getInstance().getNumberOfRelationshipsOfUser(username));
    }

    private static void createChat() {
        Chat chat = Chat.builder()
                .title("channel_test")
                .link("chnt")
                .chatType(ChatType.CHANNEL).build();

        System.out.println(ChatRepo.getInstance().addChat(chat));
    }

    private static void addUserToChat() {
        String username = "sara";
        long chatID = 1;

        System.out.println(ChatRepo.getInstance().addUserToChat(username, chatID));
    }

    private static void addPermissionToUserChat() {
        Permission permission = Permission.ADMIN;
        long userId = 3;
        long chatId = 1;

        System.out.println(PermissionRepo.getInstance().addPermissionToUserChat(permission, userId, chatId));
    }

    private static void sendMessage() {
        Message message = Message.builder()
                .messageType(MessageType.MESSAGE)
                .data("hello")
                .senderId(3L)
                .chatId(1L)
                .build();

        System.out.println(MessageRepo.getInstance().sendMessage(message));
    }

    private static void editMessage() {
        String newMessage = "hi";
        long messageId = 2;

        System.out.println(MessageRepo.getInstance().editMessage(newMessage, messageId));
    }

    private static void deleteMessage() {
        long messageId = 5;

        System.out.println(MessageRepo.getInstance().deleteMessage(messageId));
    }

    private static void getMessagesOfUser() {
        String username = "hamid";

        System.out.println(MessageRepo.getInstance().getMessagesOfUser(username));
    }

    private static void getNumberOfMessagesOfUSer() {
        String username = "hamid";

        System.out.println(MessageRepo.getInstance().getNumberOfMessagesOfUser(username));
    }

    private static void averageNumberOfMessages() {
        System.out.println(MessageRepo.getInstance().getAvgNumberOfMessages());
    }

    private static void readMessage() {
        String username = "hami";
        long messageId = 5;

        System.out.println(MessageRepo.getInstance().readMessage(username, messageId));
    }

    private static void getNumberOfViewsOfMessage() {
        long messageId = 5;

        System.out.println(MessageRepo.getInstance().getNumberOfViewsOfMessage(messageId));
    }
}
