package ir.shelmossenger;

import ir.shelmossenger.model.User;
import ir.shelmossenger.repositories.UserRepo;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
//        signup();
//        login();
//        deleteAccount();
        changeBio();
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
}
