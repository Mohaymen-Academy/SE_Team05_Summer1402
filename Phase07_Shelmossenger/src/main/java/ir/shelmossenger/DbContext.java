package ir.shelmossenger;

import ir.shelmossenger.model.User;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class DbContext {
    private DataSource dataSource;

    public DbContext() {
        dataSource = createDataSource();
    }

    private Connection getConnection() {
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private DataSource createDataSource() {
        // The url specifies the address of our database along with username and password credentials
        // you should replace these with your own username and password
        String dbName = "shelmossenger";
        String userName = "postgres";
        String password = "1234";
        String url =
                MessageFormat.format("jdbc:postgresql://localhost:5432/{0}?user={1}&password={2}", dbName, userName, password);
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    public void signup(User user) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(
                    "insert into users (full_name, user_name, email, phone_number, bio)\n" +
                            "values (?, ?, ?, ?, ?);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        stmt.setString(1, user.getFullName());
        stmt.setString(2, user.getUserName());
        stmt.setString(3, user.getEmail());
        stmt.setString(4, user.getPhoneNumber());
        stmt.setString(5, user.getBio());
// Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



