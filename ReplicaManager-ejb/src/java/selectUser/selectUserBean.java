/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selectUser;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import resources.User;

/**
 *
 * @author alessandrotorcetta
 */
@Stateless
public class selectUserBean implements selectUserBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public User selectUser(String email, String password){
        System.out.println("Dentro selectUserBean ReplicaManager");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        System.out.println("Begin Trasaction");
        TypedQuery<User> query = (TypedQuery<User>) entitymanager.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        System.out.println("Dopo aver effettuato la query");
        List<User> results = query.getResultList();
        entitymanager.close();
        if(results != null){
            User user = results.get(0);
            System.out.println("id>>" + user.toString());
            System.out.println("name>>" + user.getName());
            System.out.println("email>>" + user.getEmail());
            System.out.println("password>>" + user.getPassword());
            return user;
        }
        else{
            System.err.println("UTENTE NON TROVATO");
            return null;
        }
        
    }
    
    
}
