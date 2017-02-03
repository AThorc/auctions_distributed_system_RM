/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insertUser;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import resources.User;

/**
 *
 * @author alessandrotorcetta
 */
@Stateless
public class insertUserBean implements insertUserBeanLocal{

    public boolean insertUser(String email, String name,String password){
        System.out.println("Dentro insertUserBean ReplicaManager");
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "tesina_persistence" );
        EntityManager entitymanager = emfactory.createEntityManager( );
        entitymanager.getTransaction( ).begin( );
        System.out.println("Begin Trasaction");
        TypedQuery<User> query = (TypedQuery<User>) entitymanager.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.name = :name");   
         query.setParameter("email", email);
         query.setParameter("password", password);
         query.setParameter("name", name);
         System.out.println("Dopo aver effettuato la query");
         System.out.println("---------->query" +query.getResultList()); 
         List<User> results = query.getResultList();
         if(!results.isEmpty()){
             
            System.out.println("---------->results" +results.get(0));   
            System.out.println("---------->_>_>_>__>_>PRIMA DI GET 0");
            User user = results.get(0);
            System.out.println("---------->_>_>_>__>_>DOPOOOOOOOOOO");
            entitymanager.close();
            if(user != null){
               System.out.println("id>>" + user.toString());
               System.out.println("name>>" + user.getName());
               System.out.println("email>>" + user.getEmail());
               System.out.println("password>>" + user.getPassword());
               System.err.println("UTENTE GIà TROVATO!!!!");
               
            } 
            return false;
         }   
        else{
            System.out.println("Dentro else");
            EntityManager em = emfactory.createEntityManager( );
            EntityTransaction et = em.getTransaction( );
            et.begin();
            System.out.println("Dentro seconda transaction....");
            //em.createNativeQuery("INSERT INTO users (email,name,password, banned) VALUES ('"+ email+"','"+ name+ "','"+ password + "','"+ "000" +"');").executeUpdate();
            User u = new User();
            
            u.setEmail(email);
            u.setBanned((short) 000);
            u.setPassword(password);
            u.setName(name);
            
            
            
            System.out.println("Begin Trasaction");

            em.persist(u);
            
            et.commit();
            em.close();
             System.out.println("Utente inserito correttamente!!!");
            return true;
        }
        
    }
}
