package Model;

public class CountDown extends Thread {
    public void run(int cooldown)
    {
        while(cooldown > 0 ) {
            cooldown--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted cooldown thread");
            }
        }
    }
}
