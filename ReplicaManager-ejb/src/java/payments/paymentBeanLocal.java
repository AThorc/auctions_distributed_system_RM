/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payments;

import javax.ejb.Local;

/**
 *
 * @author alessandrotorcetta
 */
@Local
public interface paymentBeanLocal {

    public void makePay(int user_id, int item_id);
    
}
