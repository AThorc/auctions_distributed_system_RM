/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import resources.Item;
import resources.User;

/**
 *
 * @author Nick F
 */
@Stateless
public class selectLiveItemsBean implements selectLiveItemsBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public List<Item> selectLiveItems(){
        GregorianCalendar today = new GregorianCalendar();
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        System.out.println("Begin Trasaction");
        TypedQuery<Item> query = (TypedQuery<Item>) entitymanager.createQuery("SELECT i FROM Item i WHERE i.expiringDate > :curdate", Item.class);
        query.setParameter("curdate", today.getTime());
        List<Item> result = query.getResultList();
        entitymanager.close();
        return result;
    }
}
