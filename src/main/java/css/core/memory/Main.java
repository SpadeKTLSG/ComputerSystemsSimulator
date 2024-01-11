package css.core.memory;


import css.core.process.ProcessA;
import css.core.process.ProcessScheduling;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Main {
    ApplicationContext context =
            new ClassPathXmlApplicationContext("spring-config.xml");
    ProcessScheduling processScheduling = (ProcessScheduling) context.getBean("processScheduling");
    public static void main(String[] args) throws IOException {

        // Test MemoryManager functionality
        MemoryManager memoryManager = new MemoryManager();

        // Allocate memory for Process 1
        memoryManager.allocateMemory(1, "X=1");


        // Display memory status after allocation
        memoryManager.displayMemory();

        // Allocate memory for Process 2
        memoryManager.allocateMemory(2, "Y++");

        // Display memory status after allocation
        memoryManager.displayMemory();
        memoryManager.releaseMemory(1);
        memoryManager.displayMemory();

        // Allocate memory for Process 3 (should fail)
        memoryManager.allocateMemory(1, "Z=3");

        // Display memory status after allocation
        memoryManager.displayMemory();


        memoryManager.releaseMemory(2);

        memoryManager.displayMemory();

        memoryManager.allocateMemory(2, "Y++");

        // Display memory status after allocation
        memoryManager.displayMemory();

      new ProcessA("src\\main\\java\\css\\core\\memory\\api\\info.txt").start();
      new ProcessA("src\\main\\java\\css\\core\\process\\api\\info.txt").start();
       processScheduling.use();

    }
}