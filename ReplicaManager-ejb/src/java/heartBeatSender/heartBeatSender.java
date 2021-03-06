/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heartBeatSender;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import netConf.NetworkConfigurator;
import netConf.NetworkNode;

/**
 *
 * @author alessandrotorcetta
 * 
 * CLASSE RUNNABLE NON PIU UTILIZZATA. RIMPIAZZATA DA STARTUPHBSENDERBEAN.
 */
public class heartBeatSender implements Runnable{

    
    
    @Override
    public void run() {
        while(true){
            System.out.println("Sono il Thread--->" + Thread.currentThread().getName());
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
            //System.out.println(heartBeat);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(heartBeatSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   //while
    }

    private static void heartBeatReceive(java.lang.String heartBeat) {
        System.out.println("Sto per spedire L'hearBeat a tutti i FRONTEND");
        heartbeatreceiver.HeartBeatReceiverWebService_Service service = new heartbeatreceiver.HeartBeatReceiverWebService_Service();
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
        
    }
    
}