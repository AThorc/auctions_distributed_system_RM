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


public class totalOrderMulticastReceiver {

    offersBeanLocal offersBean = lookupoffersBeanLocal();

    private static totalOrderMulticastReceiver _instance = new totalOrderMulticastReceiver();
    private Map<Integer, List<TotalOrderMulticastMessage>> holdbackQueueTable;
    private Map<Integer, Integer> groupLastSequence;
    private Map<Integer, Map<Integer, List<Integer>>> groupProposalSequence;
    private Map<Integer, Integer> groupMessageCounter;
    private Map<Integer, Map<Integer, TotalOrderMulticastMessage>> bufferMessageTable;
    //private BasicMulticast basicMulticast;
    private Object _mutex;
    private List<String> deliveryQueue;
    private int  myport, myid, mygroup, mygroupsize;
    private String myname, myip;
    
    
    
    private totalOrderMulticastReceiver() {
      //  try {
                       
            /*
            File config = new File("src/java/config.txt");
            
            String myconfig;
            String rmconfig;
            
            Scanner s = new Scanner(config);
            
            myconfig  = s.nextLine();
            //rmconfig = s.nextLine();
            */
            /*
            String myconfig = "2 127.0.0.1 8080 RM1 1";
            String[] aux = myconfig.split(" ");
            
            
            
            myid = Integer.parseInt(aux[0]);
            myip = aux[1];
            myport = Integer.parseInt(aux[2]);
            myname = aux[3];
            mygroup = Integer.parseInt(aux[4]);
            mygroupsize = 2;
            */
            NetworkConfigurator nc = NetworkConfigurator.getInstance(true);
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
        _mutex = new Object();
        //deliveryQueue = new LinkedList<String>();
    }

    public static totalOrderMulticastReceiver getInstance() {
        return _instance;
    }
/*
    public void send(int groupId, String groupMessage) {
        synchronized (_mutex) {
            TotalOrderMulticastMessage tomm;
            int selfId = Profile.getInstance().getId();
            int messageId;
            Map<Integer, TotalOrderMulticastMessage> cachedMessage;

            tomm = new TotalOrderMulticastMessage();
            tomm.setContent(groupMessage);
            tomm.setSource(selfId);
            tomm.setGroupId(groupId);
            tomm.setMessageType(TotalOrderMessageType.INITIAL);
            tomm.setSequence(-1);
            if (groupMessageCounter.containsKey(groupId) == false) {
                groupMessageCounter.put(groupId, 0);
            }

            if (bufferMessageTable.containsKey(groupId) == false) {
                bufferMessageTable.put(groupId, new Hashtable<Integer, TotalOrderMulticastMessage>());
            }
            messageId = groupMessageCounter.get(groupId);
            cachedMessage = bufferMessageTable.get(groupId);
            tomm.setMessageId(messageId);
            cachedMessage.put(messageId, tomm);

            groupMessageCounter.put(groupId, messageId + 1);
            //basicMulticast.send(groupId, tomm);
            
        }
    }
    */

    public void replyProposedTs(String msg) {
        System.out.println("TotalOrderReceiver (id" + myid + ") replying with msg: " + msg);
        proposed(msg);
    }

    public void delivery(IMessage message) {
        //synchronized (_mutex) {
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
                groupLastSequence.put(groupId, 0);
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
                replyProposedTs(reply.toString());
            } else if (messageType == TotalOrderMessageType.PROPOSAL) {
                System.out.println("TotalOrderReceiver (id" + myid + ") received PROPOSAL msg. Something went wrong.");
                List<Integer> cachedSequence;
                Map<Integer, List<Integer>> cachedSequenceTable;
                int messageId = tomm.getMessageId();
                int proposeSequence = tomm.getSequence();
                if (groupProposalSequence.containsKey(groupId) == false) {
                    groupProposalSequence.put(groupId, new Hashtable<Integer, List<Integer>>());
                }
                cachedSequenceTable = groupProposalSequence.get(groupId);
                if (cachedSequenceTable.containsKey(messageId) == false) {
                    cachedSequenceTable.put(messageId, new LinkedList<Integer>());
                }

                cachedSequence = cachedSequenceTable.get(messageId);
                cachedSequence.add(proposeSequence);
                //System.out.println("receive proposed message: " + tomm);
         //       if (cachedSequence.size() == MemberIndexer.getInstance().getGroupSize(groupId)) {
                if (cachedSequence.size() == mygroupsize) {
                    int finalSequence = 0;
                    finalSequence = sequence > Collections.max(cachedSequence) ? sequence : Collections.max(cachedSequence);
                    TotalOrderMulticastMessage finalMessage = bufferMessageTable.get(groupId).get(messageId);
                    finalMessage.setSequence(finalSequence);
                    finalMessage.setMessageType(TotalOrderMessageType.FINAL);
                    //basicMulticast.send(groupId, finalMessage);
                    bufferMessageTable.get(groupId).remove(finalMessage);
                    groupLastSequence.put(groupId, finalSequence);
                    // IL SENDER PRENDE IL PROPOSAL MSG, elabora il final e lo tramsette a tutti i membri del gruppo in formato JSON
                    //basicMulticast(finalMessage.toString());
                    

                }

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
                /*
				System.out.println("======================");
				Iterator<TotalOrderMulticastMessage> iterator = priorityQueue.iterator();
				while(iterator.hasNext()){
					System.out.println(iterator.next());
				}
				
				System.out.println("======================");
                 */

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
        //} fine synch mytex
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

    private static void proposed(java.lang.String proposedTs) {
        webservtotalorder.PrTsWebService_Service service = new webservtotalorder.PrTsWebService_Service();
        webservtotalorder.PrTsWebService port = service.getPrTsWebServicePort();
        //port.proposed(proposedTs);
        
        BindingProvider bindingProvider; //classe che gestisce il cambio di indirizzo quando il webservice client deve riferirsi a webservice che stanno su macchine diverse
        bindingProvider = (BindingProvider) port;

        for(Iterator it = NetworkConfigurator.getInstance(true).getReplicas().listIterator(); it.hasNext();){
            NetworkNode n = (NetworkNode) it.next();
            
            bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://"+n.getIp()+":"+n.getPort()+"/FrontEnd-war/prTsWebService"
            );
            port.proposed(proposedTs);
        }
        
        
    }

    

    
    
    


}
