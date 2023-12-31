package css.core.memory;


import static css.core.memory.MemoryManager.threadMemoryMap;

public class Main {
    public static void main(String[] args) {
        // Test MemoryManager functionality
        MemoryManager memoryManager = new MemoryManager();

        // Allocate memory for Process 1
        memoryManager.allocateMemory(1, "X=1");
//        for(int i=0;i<64;i++){
//
//        }

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








        memoryManager.releaseMemory(2);

        memoryManager.displayMemory();

        memoryManager.allocateMemory(2, "Y++");

        // Display memory status after allocation
        memoryManager.displayMemory();
    }
}