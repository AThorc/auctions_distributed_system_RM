/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totalOrderReplicazione;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.BindingProvider;
import netConf.NetworkConfigurator;
import netConf.NetworkNode;
import offers.offersBeanLocal;
import totalOrderReply.replyMsg;


public class totalOrderMulticastReceiver {

    offersBeanLocal offersBean = lookupoffersBeanLocal();

    private static totalOrderMulticastReceiver _instance = new totalOrderMulticastReceiver();
    private Map<Integer, List<TotalOrderMulticastMessage>> holdbackQueueTable;
    private Map<Integer, Integer> groupLastSequence;
    private Map<Integer, Map<Integer, List<Integer>>> groupProposalSequence;
    private Map<Integer, Integer> groupMessageCounter;
    private Map<Integer, Map<Integer, TotalOrderMulticastMessage>> bufferMessageTable;
    //private BasicMulticast basicMulticast;
    private Object _mutex2;
    private List<String> deliveryQueue;
    private int  myport, myid, mygroup, mygroupsize;
    private String myname, myip;
    
    
    
    NetworkConfigurator nc = NetworkConfigurator.getInstance(true);
    
    
    private totalOrderMulticastReceiver() {
     
            _mutex2 = new Object();
            
          
            NetworkNode nn = nc.getMyself();
            
            myid = nn.getId();
            myip = nn.getIp();
            myport = nn.getPort();
            myname = nn.getName();
            mygroup = 1;
            mygroupsize = nc.getReplicas().size()+1;
            
            
            
            System.out.println("MIO ID-->" + myid + "MIO IP--->" + myip + "MIO NOME--->" + myname);
            
         //   } catch (FileNotFoundException ex) {
         //   Logger.getLogger(totalOrderMulticastReceiver.class.getName()).log(Level.SEVERE, null, ex);
        //}
        
        
        
        
        
        
        holdbackQueueTable = new Hashtable<Integer, List<TotalOrderMulticastMessage>>();
        groupLastSequence = new Hashtable<Integer, Integer>();
        //basicMulticast = BasicMulticast.getInstance();
        bufferMessageTable = new Hashtable<Integer, Map<Integer, TotalOrderMulticastMessage>>();
        groupProposalSequence = new Hashtable<Integer, Map<Integer, List<Integer>>>();
        groupMessageCounter = new Hashtable<Integer, Integer>();
        //_mutex = new Object();
        //deliveryQueue = new LinkedList<String>();
    }
    
    
    
    

    public synchronized static totalOrderMulticastReceiver getInstance() {
        return _instance;
    }

/*
    public  void replyProposedTs(String msg, String ip, int port) {
        System.out.println(" Dentro reply proposed Ts ---> TotalOrderReceiver (id" + myid + ") replying with msg: " + msg);
        proposed(msg, ip, port);
    }
*/
    public  replyMsg delivery(IMessage message) {
        System.out.println("RECEIVER PRIMA DI SYNCHRONIZED");
        //synchronized (_mutex2) {
            System.out.println("RECEIVER DOPO SYNCHRONIZED");
        //synchronized(this){
            TotalOrderMulticastMessage tomm = (TotalOrderMulticastMessage) message;
            int groupId;
            int sequence;
            int selfId;
            List<TotalOrderMulticastMessage> priorityQueue;
            TotalOrderMessageType messageType;

            groupId = tomm.getGroupId();
            //selfId = Profile.getInstance().getId();
            selfId = myid;
            
            if (holdbackQueueTable.containsKey(groupId) == false) {
                holdbackQueueTable.put(groupId, new LinkedList<TotalOrderMulticastMessage>());
            }
            if (groupLastSequence.containsKey(groupId) == false) {
                groupLastSequence.put(groupId, selfId);
            }

            priorityQueue = holdbackQueueTable.get(groupId);
            messageType = tomm.getMessageType();
            sequence = groupLastSequence.get(groupId);
            
            System.out.println("TotalOrderReceiver (id" + myid + ") received msg: " + tomm.toString());
            System.out.println("TotalOrderReceiver (id" + myid + ") priority Queue size: " + priorityQueue.size());

            //System.out.println("receive message: "+ tomm);
            if (messageType == TotalOrderMessageType.INITIAL) {
                System.out.println("TotalOrderReceiver (id" + myid + ") received INITIAL msg.");
                TotalOrderMulticastMessage reply;
                sequence += 1;
                groupLastSequence.put(groupId, sequence);
                
                //tomm.getSource();
                NetworkNode nn;
                String myInitiatorIp = null;
                int myInitiatorPort = 0;
                for(int i = 0; i < nc.getFrontends().size(); i++){
                    nn = (NetworkNode) nc.getFrontends().toArray()[i];
                    if(tomm.getSource() == nn.getId()){
                        myInitiatorIp = nn.getIp();
                        myInitiatorPort = nn.getPort();
                        break;
                    }
                }
                
                
                
                reply = new TotalOrderMulticastMessage();
                reply.setSource(selfId);
                reply.setGroupId(groupId);
                reply.setMessageType(TotalOrderMessageType.PROPOSAL);
                reply.setSequence(sequence);
                reply.setMessageId(tomm.getMessageId());
                //System.out.println("reply message" + reply);
                //basicMulticast.reply(groupId, tomm.getSource(), reply);
                

                tomm.setSequence(sequence);
                priorityQueue.add(tomm);
                Collections.sort(priorityQueue);
                System.out.println("TotalOrderReceiver (id" + myid + ") priority Queue size (after add): " + priorityQueue.size());
                
                //System.out.println("Sono il RECEIVER, INVIO IL NOTIFY");
                //_mutex.notifyAll();
                replyMsg r = new replyMsg(reply.toString(), myInitiatorPort, myInitiatorIp);
                //replyProposedTs(reply.toString(),myInitiatorIp,myInitiatorPort);
            
                return r;
            
            
            } else if (messageType == TotalOrderMessageType.FINAL) {
                System.out.println("TotalOrderReceiver (id" + myid + ") received FINAL msg. Will deliver some msgs soon.");
                int source = tomm.getSource();
                int mid = tomm.getMessageId();
                System.out.println("Priority Queue Size: " + priorityQueue.size());
                for (TotalOrderMulticastMessage entry : priorityQueue) {
                    System.out.println("Dentro for delivery FINAL - TO Receiver");
                    int entryMID = entry.getMessageId();
                    int entrySource = entry.getSource();
                    if (entryMID == mid && entrySource == source) {
                        entry.setMessageType(TotalOrderMessageType.FINAL);
                        entry.setSequence(tomm.getSequence());
                    }
                }
                if (sequence < tomm.getSequence()) {
                    sequence = tomm.getSequence();
                    groupLastSequence.put(groupId, sequence);
                }

                Collections.sort(priorityQueue);
              

                while (priorityQueue.isEmpty() == false) {
                    System.out.println("Dentro while delivery FINAL - TO Receiver");
                    TotalOrderMulticastMessage entry = priorityQueue.get(0);
                    //System.out.println(entry);
                    if (entry == null || entry.isDeliverable() == false) {
                        break;
                    } else if (entry.isDeliverable()) {
                        System.out.println("deliver total order message: " + entry.getContent());
                        prepareAndDoOffer(entry.toString());
                        priorityQueue.remove(0);//();
                        
                        
                    }
                }
            }
            return null;
       // } //fine synch mutex
    }
    
    
    public void prepareAndDoOffer(String deliverableMsg){
        //SPACCHETTAMENTO DOPPIO
        System.out.println("TotalOrderReceiver (id" + myid + ") delivering msg: " + deliverableMsg);
        JsonObject jo = Json.createReader(new StringReader(deliverableMsg)).readObject();       
        String content = jo.getString("content");
        
        JsonObject jo2 = Json.createReader(new StringReader(content)).readObject();
        int item_id = Integer.parseInt(jo2.getString("item_id"));
        float requested_price = (float)jo2.getJsonNumber("requestedPrice").doubleValue();
        int user_id = Integer.parseInt(jo2.getString("user_id"));
        System.out.println("ESEGUO IL RILANCIO DELL'ASTA");
        offersBean.offerPriceforItem(item_id, requested_price, user_id);
        
        
    }
    
    
/****************************************************************************/
    
    

    private offersBeanLocal lookupoffersBeanLocal() {
        try {
            Context c = new InitialContext();
            return (offersBeanLocal) c.lookup("java:global/ReplicaManager/ReplicaManager-ejb/offersBean!offers.offersBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    /*private static void proposedTs(java.lang.String proposedTs) {
        webservtotalorder.PrTsWebService_Service service = new webservtotalorder.PrTsWebService_Service();
        webservtotalorder.PrTsWebService port = service.getPrTsWebServicePort();
        port.proposedTs(proposedTs);
    }*/

    

}
