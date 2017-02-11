/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payments;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import resources.*;

/**
 *
 * @author alessandrotorcetta
 */
@Stateless
public class paymentBean implements paymentBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void makePay(int user_id, int item_id){
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        System.out.println("Begin Trasaction inside makePay del BEAN");
        
        /*
        TypedQuery<User> query = (TypedQuery<User>) entitymanager.createQuery("SELECT u FROM User u WHERE u.id = :id");   
        query.setParameter("id", user_id);
        User u = null;
        Item i = null;
        if(query.getSingleResult() != null) u = query.getSingleResult();
        Transaction t;
        TypedQuery<Item> query2 = (TypedQuery<Item>) entitymanager.createQuery("SELECT i FROM Item i WHERE i.id = :id");
        query2.setParameter("id", item_id);
        if(query2.getSingleResult() != null) i = query2.getSingleResult();
        */
        /*
        TypedQuery<Transaction> query3 = (TypedQuery<Transaction>) entitymanager.createQuery("SELECT t FROM Transaction t WHERE t.userId = :user_id AND t.itemId = :item_id");
        query3.setParameter("user_id", user_id);
        query3.setParameter("item_id", item_id);
        
        if(query3.getSingleResult()!= null) t = query3.getSingleResult();
        */
                
        System.out.println("Dentro RM BEAN PAYM user id --> " + user_id + " item id----> " +item_id);
        Query query = entitymanager.createQuery("UPDATE Transaction SET successful = :successful WHERE userId = :user_id AND itemId = :item_id");
        query.setParameter("successful", true);
        query.setParameter("user_id", entitymanager.find(User.class, user_id));
        query.setParameter("item_id", entitymanager.find(Item.class, item_id));
        User u = entitymanager.find(User.class, user_id);
        Item i = entitymanager.find(Item.class, item_id);
        System.out.println("UTENTE TROVATO---> " +u.getId() +" ---> " +  u.getName());
        System.out.println("ITEM TROVATO---> " +i.getId() +" ---> " +  i.getTitle());
        //System.out.println("Risultato query---> " + query.getResultList().toString());
        //if(query.getSingleResult()!= null) query.executeUpdate();
        query.executeUpdate();
        System.out.println("RM payBean eseguo il commit -----############");
        entitymanager.getTransaction().commit();
        entitymanager.close();
        
        
        
        
    }
    
    
}
