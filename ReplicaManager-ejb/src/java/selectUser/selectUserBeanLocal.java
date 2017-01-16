/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selectUser;

import javax.ejb.Local;
import resources.User;

/**
 *
 * @author alessandrotorcetta
 */
@Local
public interface selectUserBeanLocal {

    public User selectUser(String email, String password);
    
}
