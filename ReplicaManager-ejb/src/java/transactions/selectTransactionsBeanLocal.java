/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions;

import java.util.List;
import javax.ejb.Local;
import resources.Transaction;

/**
 *
 * @author alessandrotorcetta
 */
@Local
public interface selectTransactionsBeanLocal {

    public List<Transaction> selectTransactions();
    
}
