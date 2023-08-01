package ir.shelmossenger.context;

import ir.shelmossenger.model.Message;
import ir.shelmossenger.model.MessageType;
import ir.shelmossenger.model.User;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DbContext {
    private static final DataSource dataSource= createDataSource();

    public static Connection getConnection() {
        Connection conn;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    private static DataSource createDataSource() {
        // The url specifies the address of our database along with username and
        // password credentials
        // you should replace these with your own username and password
        String dbName = "shelmossenger";
        String userName = "postgres";
        String password = "1234";
        String url = MessageFormat.format("jdbc:postgresql://localhost:5432/{0}?user={1}&password={2}", dbName,
                userName, password);
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

}
