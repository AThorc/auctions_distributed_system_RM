/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import resources.Item;

/**
 *
 * @author Nick F
 */
@Stateless
public class deleteItemBean implements deleteItemBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void deleteItem(int id){
        System.out.println("Deleting item with ID: " + id);
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        Item i = entitymanager.find(Item.class, id);
        entitymanager.remove(i);
        entitymanager.flush();
        entitymanager.getTransaction().commit();
        entitymanager.close();
    }
}
