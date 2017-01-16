/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import resources.User;
import selectUser.selectUserBeanLocal;

/**
 *
 * @author alessandrotorcetta
 */
@WebService(serviceName = "userWebService")
public class userWebService {

    @EJB
    private selectUserBeanLocal selectUserBean;
    

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "select")
    public String selectUser(@WebParam(name = "email") String email, @WebParam(name = "password") String password) {
         User u = selectUserBean.selectUser(email, password);
         JsonObjectBuilder job = (JsonObjectBuilder) Json.createObjectBuilder();
         JsonObject jo;
         if(u != null){
            job.add("email", u.getEmail());
            job.add("name", u.getName());
            job.add("password", u.getPassword());
            jo = job.build();
         }
         else {
             job.addNull("email");
             jo = job.build();
         }
         return jo.toString();
    }
}
