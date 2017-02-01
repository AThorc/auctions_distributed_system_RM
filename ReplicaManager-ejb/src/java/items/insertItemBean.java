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
import resources.Item;
import resources.Seller;

/**
 *
 * @author Nick F
 */
@Stateless
public class insertItemBean implements insertItemBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void insertItem(String title, float price, int seller_id, long expiring_date){
        Date d = new Date();
        d.setTime(expiring_date);
        Item i = new Item();
        i.setTitle(title);
        i.setPrice(price);
        i.setExpiringDate(d);
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        i.setSellerId(entitymanager.find(Seller.class, seller_id));
        entitymanager.persist(i);
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
    
}
