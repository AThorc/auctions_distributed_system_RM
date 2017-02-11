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
    public void makePay(int user_id, int item_id) {
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("tesina_persistence");
        EntityManager entitymanager = emfactory.createEntityManager();
        entitymanager.getTransaction().begin();
        System.out.println("Begin Trasaction inside makePay del BEAN");

        System.out.println("Dentro RM BEAN PAYM user id --> " + user_id + " item id----> " + item_id);
        Query query = entitymanager.createQuery("UPDATE Transaction SET successful = :successful WHERE userId = :user_id AND itemId = :item_id");
        query.setParameter("successful", true);
        query.setParameter("user_id", entitymanager.find(User.class, user_id));
        query.setParameter("item_id", entitymanager.find(Item.class, item_id));
        User u = entitymanager.find(User.class, user_id);
        Item i = entitymanager.find(Item.class, item_id);
        System.out.println("UTENTE TROVATO---> " + u.getId() + " ---> " + u.getName());
        System.out.println("ITEM TROVATO---> " + i.getId() + " ---> " + i.getTitle());
        //System.out.println("Risultato query---> " + query.getResultList().toString());
        //if(query.getSingleResult()!= null) query.executeUpdate();
        query.executeUpdate();
        System.out.println("RM payBean eseguo il commit -----############");
        entitymanager.getTransaction().commit();
        entitymanager.close();

    }

}
