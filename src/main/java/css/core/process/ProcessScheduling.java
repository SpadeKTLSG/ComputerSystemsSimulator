package css.core.process;

import css.out.device.DeviceManagement;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

@Component
public class ProcessScheduling {
    /**
     * 就绪队列
     */
    volatile public static ArrayBlockingQueue<ProcessA> readyQueues = new ArrayBlockingQueue<ProcessA>(10);

    /**
     * 阻塞队列
     */
    volatile public static ArrayBlockingQueue<ProcessA> blocking = new ArrayBlockingQueue<ProcessA>(10);

    /**
     * 正在运行的进程
     */
    volatile public static ProcessA runing = null;

    @Transactional
    public void getReadyToRun() throws InterruptedException {
        ProcessA first = readyQueues.take();
        runing = first;
        first.pcb.state = 1;
        synchronized (first){
            first.notifyAll();
        }
    }
    @Transactional
    public void getRunToReady() throws InterruptedException {
        runing.pcb.state = 0;
        readyQueues.add(runing);
        runing.wait();
    }
    @Transactional
    public void changeProcess(String fileName) throws IOException {
        ProcessA process = new ProcessA(fileName);
        readyQueues.add(process);
        process.start();
    }
    @Transactional
    public void changeBlocking(ProcessA process) throws InterruptedException {
        runing = null;
        process.pcb.state = 2;
        process.wait();
    }

    @Transactional
    public void changeReady(ProcessA process) {
        process.pcb.state = 0;
    }

    public void use() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (runing == null) {
                    try {
                        getReadyToRun();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (readyQueues.size() != 0) {
//                    if (runing != null) {
//                        //现停止CPU正在运行的程序
//                        try {
//                            runing.pcb.state = 0;
//                            readyQueues.add(runing);
//                            runing.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    runing = readyQueues.remove(0);
//                    runing.pcb.state = 1;
//                    runing.notify();
//                }
//            }
//        }, 200L, 100L);
    }

}
