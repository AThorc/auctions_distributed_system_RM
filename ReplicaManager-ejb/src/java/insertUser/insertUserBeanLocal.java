/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package insertUser;

import javax.ejb.Local;

/**
 *
 * @author alessandrotorcetta
 */
@Local
public interface insertUserBeanLocal {

    public boolean insertUser(String email, String name,String password);
    
}
