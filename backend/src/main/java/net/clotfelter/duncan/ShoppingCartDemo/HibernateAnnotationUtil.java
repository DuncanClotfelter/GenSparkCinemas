package net.clotfelter.duncan.ShoppingCartDemo;

import net.clotfelter.duncan.ShoppingCartDemo.entities.Cart;
import net.clotfelter.duncan.ShoppingCartDemo.entities.products.*;
import net.clotfelter.duncan.ShoppingCartDemo.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HibernateAnnotationUtil {
    private static SessionFactory sessionFactory;

    static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory buildSessionFactory() {

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate-annotation.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(Cart.class)
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(Film.class)
                // other domain classes
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }
}
