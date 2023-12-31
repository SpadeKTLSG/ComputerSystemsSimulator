package css.out.device;


import css.core.process.Pcb;
import css.core.process.ProcessA;
import css.core.process.ProcessScheduling;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ArrayBlockingQueue;


public class Device {
    ApplicationContext context =
            new ClassPathXmlApplicationContext("spring-config.xml");
    ProcessScheduling processScheduling = (ProcessScheduling) context.getBean("processScheduling");

    public String name;
    public Pcb nowProcessPcb = null;
    public ArrayBlockingQueue<ProcessDeviceUse> arrayBlockingQueue = new ArrayBlockingQueue<ProcessDeviceUse>(10);

    Device(String name) {
        this.name = name;
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    ProcessDeviceUse remove = arrayBlockingQueue.take();
                    nowProcessPcb = remove.process.pcb;
                    synchronized (this){
                        this.wait(remove.longTime);
                    }
                    processScheduling.blocking.remove(remove.process);
                    processScheduling.readyQueues.add(remove.process);
                    remove.process.pcb.state = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nowProcessPcb = null;
            }
        }).start();
    }
}
