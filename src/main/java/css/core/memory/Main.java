package css.core.memory;


import css.core.process.ProcessA;
import css.core.process.ProcessScheduling;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring-config.xml");
        ProcessScheduling processScheduling = (ProcessScheduling) context.getBean("processScheduling");
        // Test MemoryManager functionality
        new ProcessA("src/main/java/css/core/memory/api/info.txt").start();
        new ProcessA("src/main/java/css/core/process/api/info.txt").start();


        processScheduling.use();

    }
}