package ir.shelmossenger.context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DbContext {
//    private static final DataSource dataSource= createDataSource();
    private static SessionFactory sessionFactory=setUp();

//    public static Connection getConnection() {
//        Connection conn;
//        try {
//            conn = dataSource.getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return conn;
//    }
    public static Session getConnection() {
        return sessionFactory.openSession();
    }

//    private static DataSource createDataSource() {
//        // The url specifies the address of our database along with username and
//        // password credentials
//        // you should replace these with your own username and password
//        String dbName = "shelmossenger";
//        String userName = "postgres";
//        String password = "1234";
//        String url = MessageFormat.format("jdbc:postgresql://localhost:5432/{0}?user={1}&password={2}", dbName,
//                userName, password);
//        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setUrl(url);
//        return dataSource;
//    }
    protected static SessionFactory setUp() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            return new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
        return null;
    }

}
