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
//        addChat();
//        addUserToChat();
//        addPermissionToUserChat();
//        sendMessage();
//        editMessage();
//        deleteMessage();
//        getMessagesOfUser();
//        getNumberOfMessagesOfUser();
//        getAvgNumberOfMessages();
//        readMessage();
//        getNumberOfViewsOfMessage();
    }

    private static void signup() {
        User user = User.builder()
                .fullName("sara")
                .username("sara1")
                .phoneNumber("s245")
                .phoneNumber("0123456789")
                .email("sara@gmail.com")
                .bio("nothing").build();

        System.out.println(UserRepo.getInstance().signup(user));
    }

    private static void login() {
        String username = "amir123";
        String password = "a123";

        System.out.println(UserRepo.getInstance().login(username, password));
    }

    private static void deleteAccount() {
        String username = "hamed1";

        System.out.println(UserRepo.getInstance().deleteAccount(username));
    }

    private static void changeBio() {
        String username = "sara2";
        String bio = "bfs";

        System.out.println(UserRepo.getInstance().changeBio(username, bio));
    }

    private static void getNumberOfRelationshipsOfUser() {
        String username = "hamid2";

        System.out.println(UserRepo.getInstance().getNumberOfRelationshipsOfUser(username));
    }

    private static void addChat() {
        Chat chat = Chat.builder()
                .title("chTest")
                .chatType(ChatType.CHANNEL)
                .link("test_chat").build();

        System.out.println(ChatRepo.getInstance().addChat(chat));
    }

    private static void addUserToChat() {
        String username = "sara1";
        long chatId = 2;

        System.out.println(ChatRepo.getInstance().addUserToChat(username, chatId));
    }

    private static void addPermissionToUserChat() {
        Permission permission = Permission.MESSAGE;
        long userChatId = 2;

        System.out.println(PermissionRepo.getInstance().addPermissionToUserChat(permission, userChatId));
    }

    private static void sendMessage() {
        String data = "hello";
        MessageType messageType = MessageType.FILE;
        String senderUsername = "sara1";
        long chatId = 2;

        System.out.println(MessageRepo.getInstance().sendMessage(data, messageType, senderUsername, chatId));
    }

    private static void editMessage() {
        String newMessage = "hi";
        long messageId = 1;

        System.out.println(MessageRepo.getInstance().editMessage(newMessage, messageId));
    }

    private static void deleteMessage() {
        long messageId = 2;

        System.out.println(MessageRepo.getInstance().deleteMessage(messageId));
    }

    private static void getMessagesOfUser() {
        String username = "hamid2";

        System.out.println(MessageRepo.getInstance().getMessagesOfUser(username));
    }

    private static void getNumberOfMessagesOfUser() {
        String username = "hamid2";

        System.out.println(MessageRepo.getInstance().getNumberOfMessagesOfUser(username));
    }

    private static void getAvgNumberOfMessages() {
        System.out.println(MessageRepo.getInstance().getAvgNumberOfMessages());
    }

    private static void readMessage() {
        String username = "hamid2";
        long messageId = 1;

        System.out.println(MessageRepo.getInstance().readMessage(username, messageId));
    }

    private static void getNumberOfViewsOfMessage() {
        long messageId = 2;

        System.out.println(MessageRepo.getInstance().getNumberOfViewsOfMessage(messageId));
    }
}
