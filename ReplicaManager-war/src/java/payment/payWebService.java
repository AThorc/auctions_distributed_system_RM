/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payment;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import payments.paymentBeanLocal;

/**
 *
 * @author alessandrotorcetta
 */
@WebService(serviceName = "payWebService")
public class payWebService {

    @EJB
    private paymentBeanLocal paymentBean;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "doPayWebServ")
    public void doPayWebServ(@WebParam(name = "user_id") int user_id, @WebParam(name = "item_id") int item_id) {
        System.out.println("Dentro web serv server PAYMENT");
        paymentBean.makePay(user_id, item_id);
    }
}
