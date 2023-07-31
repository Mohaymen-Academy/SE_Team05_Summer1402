package ir.shelmossenger;

import ir.shelmossenger.context.DbContext;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");
        // Use the method we defined earlier to create a datasource
        DbContext context = new DbContext();
        var log = context.login("asfa", "pass");
        System.out.println(log);
    }

}
