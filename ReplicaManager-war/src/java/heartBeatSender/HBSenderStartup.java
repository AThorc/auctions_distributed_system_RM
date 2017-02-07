/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heartBeatSender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author alessandrotorcetta
 */

/*
@WebListener
public class HBSenderStartup implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Startup del Thread, schedulazione in corso...");
        heartBeatSender h1 = new heartBeatSender();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(h1, 0, TimeUnit.SECONDS);// Schedule to run every minute.
        System.out.println("Startup del Thread, Thread AVVIATO");
       
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Distruggo il THREAD");
        scheduler.shutdown(); // Important! This stops the thread.
    }

}
*/
    
