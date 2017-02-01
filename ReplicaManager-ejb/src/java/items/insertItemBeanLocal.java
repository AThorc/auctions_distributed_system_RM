/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import javax.ejb.Local;
import resources.Item;

/**
 *
 * @author Nick F
 */
@Local
public interface insertItemBeanLocal {

    public void insertItem(String title, float price, int seller_id, long expiring_date);
    
}
