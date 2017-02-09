/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heartBeatSender;

//import hbsender.HeartBeatSenderWebService_Service;
import heartbeatreceiver.HeartBeatReceiverWebService_Service;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;
import netConf.NetworkConfigurator;
import netConf.NetworkNode;

@Startup
@Singleton

public class startupHBSenderBean implements startupHBSenderBeanLocal {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/FrontEnd-war/heartBeatReceiverWebService.wsdl")
    private HeartBeatReceiverWebService_Service service;

    //@WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/ReplicaManager-war/heartBeatSenderWebService.wsdl")
    //private HeartBeatSenderWebService_Service service;

    
    
    
    @PostConstruct
    public void theMainMethod() {
        heartBeatSender t1 = new heartBeatSender();
        //Thread t = new Thread(t1);
        //t.start();
        System.out.println("AVVIAMENTO STARTUB BEAN... AVVIO HEARTBEATSENDER WEBSERV");
        
        //AVVIO WEBSERVICE per invio dell'heartbeat
        
        
        
        //init();
        
        
        
        //System.out.println("Thread sganciato");

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    //@AccessTimeout(value=5000)
    @Schedule(second = "*/5", minute = "*", hour = "*", persistent=false)
    private void init(final Timer timer) {
        /*try {
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            Logger.getLogger(startupHBSenderBean.class.getName()).log(Level.SEVERE, null, ex);
        }*/
         //while(true){
            System.out.println("Sono heartBeatSender WebService");
            //GregorianCalendar g = new GregorianCalendar();
            //g.set(GregorianCalendar.YEAR, GregorianCalendar.MONTH, GregorianCalendar.DAY_OF_MONTH, GregorianCalendar.HOUR_OF_DAY, GregorianCalendar.MINUTE, GregorianCalendar.SECOND);
            int year = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
            int month = GregorianCalendar.getInstance().get(GregorianCalendar.MONTH);
            int day = GregorianCalendar.getInstance().get(GregorianCalendar.DAY_OF_MONTH);
            int hour = GregorianCalendar.getInstance().get(GregorianCalendar.HOUR_OF_DAY);
            int minute = GregorianCalendar.getInstance().get(GregorianCalendar.MINUTE);
            int second = GregorianCalendar.getInstance().get(GregorianCalendar.SECOND);
                   
            String heartBeat = NetworkConfigurator.getInstance(true).getMyself().getId() + " Rm alive, ts: YEAR --->" + year + " MONTH --->" + month + " DAY -->" + day + " HOUR --->" + hour + " MIN -->" + minute + " SEC-->" +second ;
            heartBeatReceive(heartBeat);
             /*try {
                 Thread.sleep(5000);
             } catch (InterruptedException ex) {
                 Logger.getLogger(startupHBSenderBean.class.getName()).log(Level.SEVERE, null, ex);
             }
            
        } */
    }

    /*private void heartBeatReceive(java.lang.String heartBeat) {
        System.out.println("Sto per spedire L'hearBeat a tutti i FRONTEND");
        service = new heartbeatreceiver.HeartBeatReceiverWebService_Service();
        heartbeatreceiver.HeartBeatReceiverWebService port = service.getHeartBeatReceiverWebServicePort();
        //port.heartBeatReceive(heartBeat);
        
        BindingProvider bindingProvider; //classe che gestisce il cambio di indirizzo quando il webservice client deve riferirsi a webservice che stanno su macchine diverse
        bindingProvider = (BindingProvider) port;

        for(Iterator it = NetworkConfigurator.getInstance(true).getFrontends().listIterator(); it.hasNext();){
           // System.out.println("Sto per spedire L'hearBeat a tutti i FRONTEND");
            NetworkNode n = (NetworkNode) it.next();
            System.out.println("INVIO HEARTBEAT AL NODO--->" + n.getId());
            bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://"+n.getIp()+":"+n.getPort()+"/FrontEnd-war/heartBeatReceiverWebService"
            );
            port.heartBeatReceive(heartBeat);
        }
    }*/

    private void heartBeatReceive(java.lang.String heartBeat) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        service = new heartbeatreceiver.HeartBeatReceiverWebService_Service();
        heartbeatreceiver.HeartBeatReceiverWebService port = service.getHeartBeatReceiverWebServicePort();
        
        BindingProvider bindingProvider; //classe che gestisce il cambio di indirizzo quando il webservice client deve riferirsi a webservice che stanno su macchine diverse
        bindingProvider = (BindingProvider) port;

        for(Iterator it = NetworkConfigurator.getInstance(true).getFrontends().listIterator(); it.hasNext();){
           // System.out.println("Sto per spedire L'hearBeat a tutti i FRONTEND");
            NetworkNode n = (NetworkNode) it.next();
            System.out.println("INVIO HEARTBEAT AL NODO--->" + n.getId());
            bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                "http://"+n.getIp()+":"+n.getPort()+"/FrontEnd-war/heartBeatReceiverWebService"
            );
            try{
                port.heartBeatReceive(heartBeat);
            }catch(Exception ex){
                System.err.println("Errore di rete");
            }
        }

    }
    
    

}
