/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offers;

import javax.ejb.Local;
import resources.Transaction;

/**
 *
 * @author alessandrotorcetta
 */
@Local
public interface offersBeanLocal {

    public Transaction offerPriceforItem(int item_id, float requested_price, int user_id);

    public Transaction getTransaction(int item_id);
    
}
