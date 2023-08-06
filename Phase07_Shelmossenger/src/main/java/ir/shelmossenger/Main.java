package ir.shelmossenger;

import ir.shelmossenger.model.User;
import ir.shelmossenger.repositories.UserRepo;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
//        signup();
        login();
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
}
