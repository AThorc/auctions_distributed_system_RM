/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heartBeatSender;

import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alessandrotorcetta
 */
public class heartBeatSender implements Runnable{

    
    
    @Override
    public void run() {
     //   while(true){
            System.out.println("Sono il Thread--->" + Thread.currentThread().getName());
            //GregorianCalendar g = new GregorianCalendar();
            //g.set(GregorianCalendar.YEAR, GregorianCalendar.MONTH, GregorianCalendar.DAY_OF_MONTH, GregorianCalendar.HOUR_OF_DAY, GregorianCalendar.MINUTE, GregorianCalendar.SECOND);
            int year = GregorianCalendar.getInstance().get(GregorianCalendar.YEAR);
            int month = GregorianCalendar.getInstance().get(GregorianCalendar.MONTH);
            int day = GregorianCalendar.getInstance().get(GregorianCalendar.DAY_OF_MONTH);
            int hour = GregorianCalendar.getInstance().get(GregorianCalendar.HOUR_OF_DAY);
            int minute = GregorianCalendar.getInstance().get(GregorianCalendar.MINUTE);
            int second = GregorianCalendar.getInstance().get(GregorianCalendar.SECOND);
                   
            String heartBeat = "2 Rm alive, ts: YEAR --->" + year + " MONTH --->" + month + " DAY -->" + day + " HOUR --->" + hour + " MIN -->" + minute + " SEC-->" +second ;
            heartBeatReceive(heartBeat);
            //System.out.println(heartBeat);
           /* try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(heartBeatSender.class.getName()).log(Level.SEVERE, null, ex);
            }*/
       // }   //while
    }

    private static void heartBeatReceive(java.lang.String heartBeat) {
        heartbeatreceiver.HeartBeatReceiverWebService_Service service = new heartbeatreceiver.HeartBeatReceiverWebService_Service();
        heartbeatreceiver.HeartBeatReceiverWebService port = service.getHeartBeatReceiverWebServicePort();
        port.heartBeatReceive(heartBeat);
    }
    
}
