/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offer;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Iterator;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;
import model.TotalOrderMessageType;
import model.TotalOrderMulticastMessage;
import netConf.NetworkConfigurator;
import netConf.NetworkNode;
import offers.offersBeanLocal;
import resources.Transaction;
import totalOrderReplicazione.totalOrderMulticastReceiver;
import totalOrderReply.replyMsg;
import webservtotalorder.PrTsWebService_Service;

/**
 *
 * @author alessandrotorcetta
 */
@WebService(serviceName = "offerWebService")
public class offerWebService {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/FrontEnd-war/prTsWebService.wsdl")
    private PrTsWebService_Service service;

    @EJB
    private offersBeanLocal offersBean;

    /**
     * Web service operation
     * METODO CHE ESPONE IL WEBSERVICE PER RICEVERE MESSAGGI DI REVIE_TS E FINAL_TS.
     *  COOPERA CON LA CLASSE GESTORE DELL'ALGORITMO ISIS LATO REPLICA totalOrderMulticastReceiver.
     */
    @WebMethod(operationName = "offer")
    public void offer(@WebParam(name = "offerMsg") String offerMsg) {

        //TODO write your implementation code here:
        System.out.println("Dentro offer di offerWService");

        String msgType;
        TotalOrderMessageType mt = TotalOrderMessageType.INITIAL;

        System.out.println(offerMsg);

        JsonObject jo = Json.createReader(new StringReader(offerMsg)).readObject();
        TotalOrderMulticastMessage msg = new TotalOrderMulticastMessage();
        msg.setMessageId(jo.getInt("messageId"));
        msg.setTotalOrderSequence(jo.getInt("sequence"));
        msgType = jo.getString("messageType");

        switch (msgType) {
            case "INITIAL":
                mt = TotalOrderMessageType.INITIAL;
                System.out.println("SONO IL RM, RICEVUTO MESSAGGIO INITIAL");

                msg.setMessageType(mt);
                msg.setGroupId(jo.getInt("groupId"));
                msg.setSource(jo.getInt("source"));
                msg.setSequence(jo.getInt("sequence"));
                msg.setContent(jo.getString("content"));

                System.out.println("Sto facendo il deliver di --->" + msg.toString());

                ///------->
                replyMsg r = totalOrderMulticastReceiver.getInstance().delivery(msg);
                proposed(r.getContent(), r.getPort(), r.getIp());

                break;
            case "FINAL":
                mt = TotalOrderMessageType.FINAL;
                System.out.println("SONO IL RM, RICEVUTO MESSAGGIO FINAL");
                msg.setMessageType(mt);
                msg.setGroupId(jo.getInt("groupId"));
                msg.setSource(jo.getInt("source"));
                msg.setSequence(jo.getInt("sequence"));
                msg.setContent(jo.getString("content"));

                System.out.println("Sto facendo il deliver di --->" + msg.toString());

                ///------->
                totalOrderMulticastReceiver.getInstance().delivery(msg);

                break;
            default:
                System.out.println("Ricevuto messaggio non valido");
                break;
        }

    }

    /*  METODO CHE RESTITUISCE LO STATO DELL'ULTIMA OFFERTA PER UN OGGETTO IN ASTA. 
        VIENE INVOCATO PERIODICAMENTE DAL THREAD OFFERLIVEWATCH TRAMITE OFFERBEAN*/
    @WebMethod(operationName = "getTransaction")
    public String getTransaction(@WebParam(name = "item_id") int item_id) {
        //TODO write your implementation code here:
        System.out.println("Dentro getTransaction di offerWService");
        Transaction t = offersBean.getTransaction(item_id);

        if (t == null) {
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

    private void proposed(java.lang.String proposedTs, int porta, String ip) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        webservtotalorder.PrTsWebService port = service.getPrTsWebServicePort();
        //port.proposed(proposedTs);

        BindingProvider bindingProvider; //classe che gestisce il cambio di indirizzo quando il webservice client deve riferirsi a webservice che stanno su macchine diverse
        bindingProvider = (BindingProvider) port;

        bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://" + ip + ":" + porta + "/FrontEnd-war/prTsWebService"
        );
        //port.proposed(proposedTs);

        try {
            port.proposed(proposedTs);
        } catch (Exception ex) {
            System.err.println("Errore di rete");
        }
    }

}
