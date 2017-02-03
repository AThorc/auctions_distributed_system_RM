/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import resources.Transaction;

/**
 *
 * @author alessandrotorcetta
 */
@Stateless
public class selectTransactionsBean implements selectTransactionsBeanLocal {

   public List<Transaction> selectTransactions(){

        System.out.println("Dentro selectTransactions di viewTransactionbBean di REPLICA MANAGER");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        System.out.println("Begin selectTrasactions sono nel TransactionBean");
        TypedQuery<Transaction> query = (TypedQuery<Transaction>) entitymanager.createQuery("SELECT t FROM Transaction t", Transaction.class);

        List<Transaction> result = query.getResultList();
        entitymanager.close();
        return result;
    }
}
