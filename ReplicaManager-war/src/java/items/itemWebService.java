/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import resources.Item;

/**
 *
 * @author Nick F
 */
@WebService(serviceName = "itemWebService")
public class itemWebService {
    @EJB
    private updateItembeanLocal updateItemBean;
    @EJB
    private insertItemBeanLocal insertItemBean;
    @EJB
    private deleteItemBeanLocal deleteItemBean;
    @EJB
    private selectLiveItemsBeanLocal selectLiveItemsBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "updateTitle")
    public void updateTitle(@WebParam(name = "id") int id,@WebParam(name = "title") String title) {
        updateItemBean.updateItemTitle(id, title);
    }
    
    @WebMethod(operationName = "updatePrice")
    public void updatePrice(@WebParam(name = "id") int id, @WebParam(name = "price") float price) {
        updateItemBean.updateItemPrice(id, price);
    }
    
    @WebMethod(operationName = "updateExpiringDate")
    public void updateExpiringDate(@WebParam(name = "id") int id, @WebParam(name = "expiring_date") long expiring_date) {
        updateItemBean.updateItemExpDate(id, expiring_date);
    }
    
    @WebMethod(operationName = "insert")
    public void insert(@WebParam(name = "title") String title, 
                        @WebParam(name = "price") float price,
                        @WebParam(name = "seller_id") int seller_id,
                        @WebParam(name = "expiring_date") long expiring_date) {
        
        insertItemBean.insertItem(title, price, seller_id, expiring_date);
    }
    
    @WebMethod(operationName = "delete")
    public void delete(@WebParam(name = "id") int id) {
        deleteItemBean.deleteItem(id);
    }
    
    @WebMethod(operationName = "selectLive")
    public String selectLive() {
        Item aux;
        JsonObject jo;
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        List<Item> list = selectLiveItemsBean.selectLiveItems();
        
        for(Iterator it = list.iterator(); it.hasNext();){
            aux = (Item) it.next();
            job.add("title", aux.getTitle());
            job.add("id", aux.getId());
            job.add("price", aux.getPrice());
            job.add("seller_id", aux.getSellerId().getId());
            job.add("expiring_date", aux.getExpiringDate().getTime());
            jo = job.build();
            jab.add(jo);
        }
        return jab.build().toString();
    }
}
