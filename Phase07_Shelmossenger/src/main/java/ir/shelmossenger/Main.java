package ir.shelmossenger;

import ir.shelmossenger.model.ChatType;
import ir.shelmossenger.model.Message;
import ir.shelmossenger.model.MessageType;
import ir.shelmossenger.model.User;
import ir.shelmossenger.repositories.ChatRepo;
import ir.shelmossenger.repositories.MessageRepo;
import ir.shelmossenger.repositories.UserRepo;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
//        signup();
//        login();
//        deleteAccount();
//        changeBio();
//        createChat();
//        addUserToChat();
//        sendMessage();
        editMessage();
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

    private static void deleteAccount(){
        String username = "ham";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.deleteAccount(username));
    }

    private static void changeBio(){
        String username = "hamid";
        String bio = "do";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.changeBio(username, bio));
    }

    private static void createChat(){
        String title = "pv_test";
        String link = "pvt";
        ChatType chatType = ChatType.PV;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.createChat(title, link, chatType));
    }

    private static void addUserToChat(){
        String username = "hamid";
        long chatID = 3;

        ChatRepo chatRepo = new ChatRepo();
        System.out.println(chatRepo.addUserToChat(username, chatID));
    }

    private static void sendMessage(){
        Message message = new Message();
        message.setMessageType(MessageType.FILE);
        message.setData("error");
        message.setSenderId(5L);
        message.setChatId(4L);

        MessageRepo messageRepo= new MessageRepo();
        System.out.println(messageRepo.sendMessage(message));
    }

    private static void editMessage(){
        String newMessage = "hi";
        long messageId = 6;

        MessageRepo messageRepo = new MessageRepo();
        System.out.println(messageRepo.editMessage(newMessage, messageId));
    }
}
