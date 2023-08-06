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
        user.setId(100L);
        user.setFullName("hamed");
        user.setUserName("ham");
        user.setPassword("h123");
        user.setEmail("hamed@gmail.com");
        user.setPhoneNumber("09123457780");
//        user.setBio("another thing");

        try {
            System.out.println(userRepo.signup(user));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
