package ir.shelmossenger;

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
    private Connection getConnection(){
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
        String dbName="shelmossenger";
        String userName="postgres";
        String password="1234";
        String url =
                MessageFormat.format("jdbc:postgresql://localhost:5432/{0}?user={1}&password={2}",dbName,userName,password);
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }
    public void signup(User user){
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("SELECT * FROM users");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

// Execute the query, and store the results in the ResultSet instance
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

// We run a loop to process the results.
// The rs.next() method moves the result pointer to the next result row, and returns
// true if a row is present, and false otherwise
// Note that initially the result pointer points before the first row, so we have to call
// rs.next() the first time
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Now that `rs` points to a valid row (rs.next() is true), we can use the `getString`
            // and `getLong` methods to return each column value of the row as a string and long
            // respectively, and print it to the console
            System.out.println(MessageFormat.format("id:{0}, name:{1}, email:{2}"
                    , rs.getObject("id"), rs.getString("full_name"), rs.getString("email")));
        }
    }


}
