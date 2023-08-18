package ir.shelmossenger.context;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DbContext {

    private static final SessionFactory sessionFactory = setUp();

    public static Session getConnection() {
        return sessionFactory.openSession();
    }

    protected static SessionFactory setUp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        return new MetadataSources(registry).getMetadataBuilder().build().getSessionFactoryBuilder().build();
    }
}
