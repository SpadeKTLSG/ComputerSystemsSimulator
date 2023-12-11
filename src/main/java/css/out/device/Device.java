package css.out.device;


import css.core.process.Pcb;
import css.core.process.Process;
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
    public int lock = 0;
    public ArrayBlockingQueue<ProcessDeviceUse> arrayBlockingQueue = new ArrayBlockingQueue<ProcessDeviceUse>(10);

    Device(String name) {
        this.name = name;
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    ProcessDeviceUse remove = arrayBlockingQueue.remove();
                    lock = 1;
                    nowProcessPcb = remove.process.pcb;
                    processScheduling.blocking.remove(remove.process);
                    Process runing = processScheduling.runing;
                    runing.pcb.state = 0;
                    processScheduling.readyQueues.add(runing);
                    processScheduling.runing = remove.process;
                    remove.process.pcb.state = 1;
                    remove.process.notify();
                    this.wait(remove.longTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nowProcessPcb = null;
            }
        }).start();
    }
}
