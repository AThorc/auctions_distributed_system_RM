/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offers;

import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import resources.Item;
import resources.Transaction;
import resources.User;

/**
 *
 * @author alessandrotorcetta
 */
@Stateless
public class offersBean implements offersBeanLocal {

    public Transaction offerPriceforItem(int item_id, float requested_price, int user_id){

        System.out.println("Dentro offerPrice REPLICA MANAGER");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        entitymanager.clear();
        System.out.println("Begin offersBean  RM ---> valori di item_id ---> " + item_id + " reqPrice--> " + requested_price + " user_id --> " +user_id);
        TypedQuery<Transaction> query = (TypedQuery<Transaction>) entitymanager.createQuery("SELECT t FROM Transaction t WHERE t.itemId = :item_id", Transaction.class);
        System.out.println("Dopo query di offersBean");
        query.setParameter("item_id", entitymanager.find(Item.class, item_id));
        List<Transaction> result = query.getResultList();
        GregorianCalendar g = new GregorianCalendar();
        Transaction t;
        if(!result.isEmpty()){ 
            t = result.get(0);
            if(t.getAmount()<requested_price){
                System.out.println("Aggiornamento offerta: item id: " + item_id + " offer: " + requested_price + " user id: " + user_id);
                Query query2 = entitymanager.createQuery("UPDATE Transaction SET amount = :requested_price, userId = :user_id, timestamp = :tsp WHERE itemId = :item_id");
                query2.setParameter("requested_price", requested_price);
                query2.setParameter("item_id", entitymanager.find(Item.class, item_id));
                query2.setParameter("user_id", entitymanager.find(User.class, user_id));
                query2.setParameter("tsp", g.getTime());
                query2.executeUpdate();
                entitymanager.getTransaction().commit();
                t.setUserId(entitymanager.find(User.class, user_id));
                t.setAmount(requested_price);
                
            }else System.out.println("Offerta troppo bassa. In memoria una più alta"); 
        }
        
        else {
            GregorianCalendar gr = new GregorianCalendar();
            t = new Transaction();
            t.setUserId(entitymanager.find(User.class, user_id));
            t.setTimestamp(gr.getTime());
            t.setAmount(requested_price);
            t.setItemId(entitymanager.find(Item.class, item_id));
            entitymanager.persist(t);
            entitymanager.getTransaction().commit();
            System.out.println("Prima transazione creata: " + t.toString());
        }
        
        entitymanager.close();
        return t;
    }
    
    
    public Transaction getTransaction(int item_id){

        System.out.println("Dentro offerPrice REPLICA MANAGER - Thread invoked");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        System.out.println("Begin offersBean  RM ---> valori di item_id ---> " + item_id);
        TypedQuery<Transaction> query = (TypedQuery<Transaction>) entitymanager.createQuery("SELECT t FROM Transaction t WHERE t.itemId = :item_id", Transaction.class);
        System.out.println("Dopo query di offersBean");
        query.setParameter("item_id", entitymanager.find(Item.class, item_id));
        List<Transaction> result = query.getResultList();
       // GregorianCalendar g = new GregorianCalendar();
        Transaction t;
        if(!result.isEmpty())
         {
            t = result.get(0);
            entitymanager.close();
            return t;
         }
        entitymanager.close();
        return null;
    }
}
