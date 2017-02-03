/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offer;

import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import offers.offersBeanLocal;
import resources.Transaction;

/**
 *
 * @author alessandrotorcetta
 */
@WebService(serviceName = "offerWebService")
public class offerWebService {

    @EJB
    private offersBeanLocal offersBean;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "offer")
    public String offer(@WebParam(name = "item_id") int item_id, @WebParam(name = "requested_price") float requested_price, @WebParam(name = "user_id") int user_id) {
        //TODO write your implementation code here:
        System.out.println("Dentro offer di offerWService");
        Transaction t = offersBean.offerPriceforItem(item_id, requested_price, user_id);

        JsonObjectBuilder job = (JsonObjectBuilder) Json.createObjectBuilder();
        JsonObject jo;
        job.add("user_id", t.getUserId().getId());
        job.add("id", t.getId());
        
        job.add("exist", true);
        job.add("timestamp", t.getTimestamp().getTime());
        job.add("item_id", t.getItemId().getId());
        job.add("amount", t.getAmount());
        job.add("successful", t.getSuccessful());
        jo = job.build();

        return jo.toString();

    }
    
    @WebMethod(operationName = "getTransaction")
    public String getTransaction(@WebParam(name = "item_id") int item_id) {
        //TODO write your implementation code here:
        System.out.println("Dentro getTransaction di offerWService");
        Transaction t = offersBean.getTransaction(item_id);
        
        if(t==null){
            JsonObjectBuilder job2 = (JsonObjectBuilder) Json.createObjectBuilder();
            JsonObject jo2;
            job2.addNull("id");
            job2.add("exist", false);
            jo2 = job2.build();
            return jo2.toString();
        }
        
        JsonObjectBuilder job = (JsonObjectBuilder) Json.createObjectBuilder();
        JsonObject jo;
        job.add("user_id", t.getUserId().getId());
        job.add("id", t.getId());
        job.add("exist", true);
        job.add("timestamp", t.getTimestamp().getTime());
        job.add("item_id", t.getItemId().getId());
        job.add("amount", t.getAmount());
        job.add("successful", t.getSuccessful());
        jo = job.build();

        return jo.toString();

    }

}