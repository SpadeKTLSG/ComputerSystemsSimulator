package css.core.process;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ProcessScheduling  {
    /**
     *就绪队列
     */
    public  LinkedList<Process> readyQueues = new LinkedList<Process>();

    /**
     *阻塞队列
     */
    public  LinkedList<Process> blocking = new LinkedList<Process>();

    /**
     *正在运行的进程
     */
    public Process runing = null;

    public  void getReadyToRun() {
            Process first = readyQueues.removeFirst();
            runing = first;
            first.pcb.state = 1;
            first.notify();
    }
    public  void getRunToReady() throws InterruptedException {
        runing.pcb.state = 0;
        readyQueues.add(runing);
        runing.wait();
    }

    public  void changeProcess(String fileName) throws IOException {
        Process process = new Process(fileName);
        readyQueues.add(process);
        process.start();
    }

    public  void changeBlocking(Process process) throws InterruptedException {
        runing = null;
        process.pcb.state = 2;
        process.wait();
    }


    public  void changeReady(Process process){
        process.pcb.state = 0;
    }
    public void use(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    if (readyQueues.size()!=0){
                        if (runing!=null){
                            //现停止CPU正在运行的程序
                            try {
                                runing.pcb.state = 0;
                                readyQueues.add(runing);
                                runing.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        runing = readyQueues.remove(0);
                        runing.pcb.state = 1;
                        runing.notify();
                    }
            }
        }, 200L, 100L);
    }

}
