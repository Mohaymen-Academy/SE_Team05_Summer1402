package ir.shelmossenger;

import ir.shelmossenger.context.DbContext;
import ir.shelmossenger.model.User;
import ir.shelmossenger.repositories.UserRepo;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserRepo userRepo=new UserRepo();
        var user=new User(){{
            setUsername("sadegh");
            setEmail("sa@ds.cw");
            setPhoneNumber("0911");
            setPassword("pass");
        }};
        userRepo.signup(user);

    }

}
