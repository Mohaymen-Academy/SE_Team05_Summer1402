package ir.shelmossenger;

import ir.shelmossenger.model.User;
import ir.shelmossenger.repositories.UserRepo;

public class Main {

    public static void main(String[] args) {
        signup();
//        login();
//        deleteAccount();
//        changeBio();
    }

    private static void signup() {
        User user = new User();
        user.setFullName("sara");
        user.setUsername("sara2");
        user.setPassword("s1245");
        user.setPhoneNumber("9123456789");
        user.setEmail("sara@gmail.com");
        user.setBio("sth");

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.signup(user));
    }

    private static void login(){
        String username = "amir123";
        String password = "a123";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.login(username, password));
    }

    private static void deleteAccount(){
        String username = "hamed1";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.deleteAccount(username));
    }

    private static void changeBio(){
        String username = "sara2";
        String bio = "bfs";

        UserRepo userRepo = new UserRepo();
        System.out.println(userRepo.changeBio(username, bio));
    }
}
