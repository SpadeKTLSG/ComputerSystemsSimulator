package css.core.process;


import css.out.device.DeviceManagement;
import css.out.device.ProcessDeviceUse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class Process extends Thread {
    ApplicationContext context =
            new ClassPathXmlApplicationContext("spring-config.xml");
    ProcessScheduling processScheduling = (ProcessScheduling) context.getBean("processScheduling");
    DeviceManagement deviceManagement = (DeviceManagement) context.getBean("deviceManagement");

    public boolean stop = false;
    public Pcb pcb;
    public FileReader file;
    public BufferedReader bufferedReader;

    Process(String fileName) throws IOException {
        pcb = new Pcb();
        file = new FileReader(fileName);
        bufferedReader = new BufferedReader(file);
    }

    @Override
    public void run() {
        try {
            this.wait();
            processScheduling.readyQueues.add(this);
            while (!stop) {
                CPU();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void wirth() throws IOException {
        Iterator<Map.Entry<String, Integer>> iterator = pcb.register.entrySet().iterator();
        FileWriter fileWriter = new FileWriter("out.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            bufferedWriter.write(next.getKey() + "=" + next.getValue() + "\t\n");
        }
        bufferedWriter.close();
        fileWriter.close();
    }

    public void CPU() throws IOException, InterruptedException {
        if (pcb.state == 1) {
            String s = bufferedReader.readLine();
            pcb.lines = s;
            if (s == null) {
                pcb.state = 3;
                processScheduling.runing = null;
                processScheduling.getReadyToRun();
                this.stop = true;
            } else if (s.contains("=")) {
                String[] split = s.split("=");
                pcb.register.put(split[0], Integer.valueOf(split[1]));
            } else if (s.contains("++")) {
                String[] split = s.split("\\+\\+");
                Integer integer = pcb.register.get(split[0]);
                pcb.register.put(split[0], integer + 1);
            } else if (s.contains("--")) {
                String[] split = s.split("--");
                Integer integer = pcb.register.get(split[0]);
                pcb.register.put(split[0], integer - 1);
            } else if (s.startsWith("!")) {
                char c = s.charAt(2);
                //放入设备的等待队列中
                deviceManagement.devices.get(c).arrayBlockingQueue.put(new ProcessDeviceUse(this, s.charAt(2) - '0'));
                //将其设为阻塞状态
                pcb.state = 2;
                processScheduling.runing = null;
                //从就绪队列中选一个进程运行
                processScheduling.getReadyToRun();
                processScheduling.blocking.add(this);
                this.wait();
                //开始使用设备了
                this.wait(s.charAt(2) - '0');
            } else if (s.equals("end")) {
                bufferedReader.close();
                file.close();
                wirth();
            }
        }
    }


}
