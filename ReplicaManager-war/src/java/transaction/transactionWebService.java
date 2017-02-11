/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import resources.Transaction;
import transactions.selectTransactionsBeanLocal;


/**
 *
 * @author alessandrotorcetta
 */
@WebService(serviceName = "transactionWebService")
public class transactionWebService {

    @EJB
    private selectTransactionsBeanLocal selectTransactionsBean;
    
    @WebMethod(operationName = "selectTransactions")
    public String selectTransactions() {
        
        
        System.out.println("Dentro selectTransaction di ws del REPLICA MANAGER");
        Transaction aux;
        JsonObject jo;
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        List<Transaction> list = selectTransactionsBean.selectTransactions();
        
        for(Iterator it = list.iterator(); it.hasNext();){
            aux = (Transaction) it.next();
            job.add("id", aux.getId());
            job.add("user_id", aux.getUserId().getId());
            job.add("timestamp", aux.getTimestamp().getTime());
            job.add("item_id", aux.getItemId().getId());
            job.add("amount", aux.getAmount());
            job.add("successful", aux.getSuccessful());
            jo = job.build();
            jab.add(jo);
        }
        return jab.build().toString();
        
        
    }
    
}
