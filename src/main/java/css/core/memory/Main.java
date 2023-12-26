package css.core.memory;

import java.util.ArrayList;
import java.util.List;





public class Main {
    public static void main(String[] args) {
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

        // Allocate memory for Process 3 (should fail)
        memoryManager.allocateMemory(1, "Z=3");

        // Display memory status after allocation
        memoryManager.displayMemory();
    }
}