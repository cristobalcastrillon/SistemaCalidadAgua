import java.awt.Toolkit;
import java.util.concurrent.*;

/**
 *  * BeepClock.java  *  * This program demonstrates how to schedule a task to
 * execute after  * an initial delay, and repeat after a fixed rate.  *  
 *
 *
 * @author www.codejava.net  
 */
public class BeepClock implements Runnable {

    public void run() {
        System.out.println("ok");
        //Toolkit.getDefaultToolkit().beep();
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduler
                = Executors.newSingleThreadScheduledExecutor();

        Runnable task = new BeepClock();
        int initialDelay = 500;
        int periodicDelay = 500;
        scheduler.scheduleAtFixedRate(task, initialDelay, periodicDelay,
                TimeUnit.MILLISECONDS
        );
    }
}