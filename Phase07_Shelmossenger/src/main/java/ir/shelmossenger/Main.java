package ir.shelmossenger;

import ir.shelmossenger.model.User;
import ir.shelmossenger.repositories.UserRepo;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        signup();

//        System.out.println(userRepo.login("hosraz", "pass"));
    }

    private static void signup(){
        UserRepo userRepo = new UserRepo();
        User user = new User();
//        user.setId(100L);
        user.setFullName("sara");
        user.setUserName("sara");
        user.setPassword("s123");
        user.setEmail("sara@gmail.com");
        user.setPhoneNumber("19123457780");
        user.setBio("nothing");

        try {
            System.out.println(userRepo.signup(user));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
