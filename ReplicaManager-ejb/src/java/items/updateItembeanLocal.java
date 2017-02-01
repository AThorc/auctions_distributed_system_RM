/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.util.Date;
import javax.ejb.Local;
import resources.Item;

/**
 *
 * @author Nick F
 */
@Local
public interface updateItembeanLocal {

    public void updateItemExpDate(int id, long expiring_date);

    public void updateItemTitle(int id, String title);

    public void updateItemPrice(int id, float price);
    
}
