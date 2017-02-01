/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import resources.Item;

/**
 *
 * @author Nick F
 */
@Stateless
public class updateItemBean implements updateItembeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public void updateItemTitle(int id, String title){
        System.out.println("updateItemTitle, BEAN: updateItemBean, values= id: " + id + " , title: " + title);
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        Query query = entitymanager.createQuery("UPDATE Item SET title = :title WHERE id =:id");
        query.setParameter("title", title);
        query.setParameter("id", id);
        query.executeUpdate();
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    @Override
    public void updateItemPrice(int id, float price){
        System.out.println("updateItemPrice, BEAN: updateItemBean, values= id: " + id + " , price: " + price);
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        Query query = entitymanager.createQuery("UPDATE Item SET price = :price WHERE id =:id");
        query.setParameter("price", price);
        query.setParameter("id", id);
        query.executeUpdate();
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
    @Override
    public void updateItemExpDate(int id, long expiring_date){
        Date d = new Date();
        d.setTime(expiring_date);
        System.out.println("updateItemExpDate, BEAN: updateItemBean, values= id: " + id + " , expdate: " + expiring_date);
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        Query query = entitymanager.createQuery("UPDATE Item SET expiringDate = :exp WHERE id =:id");
        query.setParameter("exp", d);
        query.setParameter("id", id);
        query.executeUpdate();
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
}
