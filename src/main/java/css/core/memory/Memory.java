package css.core.memory;

import java.util.ArrayList;
import java.util.List;

class MemoryBlock {
    int startAddress;
    int size;
    boolean allocated;

    public MemoryBlock(int startAddress, int size) {
        this.startAddress = startAddress;
        this.size = size;
        this.allocated = false;
    }
}

class MemoryManager {
    List<MemoryBlock> memoryBlocks;

    public MemoryManager() {
        this.memoryBlocks = new ArrayList<>();
    }

    // 初始化内存
    public void initializeMemory(int totalSize) {
        memoryBlocks.clear();
        memoryBlocks.add(new MemoryBlock(0, totalSize));
    }

    // 分配内存
    public int allocateMemory(int size) {
        for (MemoryBlock block : memoryBlocks) {
            if (!block.allocated && block.size >= size) {
                block.allocated = true;
                // 返回分配的起始地址
                return block.startAddress;
            }
        }
        // 无法分配足够大小的内存块
        return -1;
    }

    // 释放内存
    public void deallocateMemory(int address) {
        for (MemoryBlock block : memoryBlocks) {
            if (block.startAddress == address && block.allocated) {
                block.allocated = false;
                break;
            }
        }
    }

    // 打印内存状态
    public void printMemoryStatus() {
        System.out.println("内存状态:");
        for (MemoryBlock block : memoryBlocks) {
            System.out.println("地址: " + block.startAddress + ", 值: " + block.size + ", 分配: " + block.allocated);
        }
        System.out.println("--------------------------");
    }
}

public class Memory {
    public static void main(String[] args) {
        MemoryManager memoryManager = new MemoryManager();

        // 初始化内存
        memoryManager.initializeMemory(1000);
        memoryManager.printMemoryStatus();

        // 分配内存
        int allocatedAddress1 = memoryManager.allocateMemory(200);
        System.out.println("分配地址: " + allocatedAddress1);
        memoryManager.printMemoryStatus();

        // 再次分配内存
        int allocatedAddress2 = memoryManager.allocateMemory(300);
        System.out.println("分配地址 : " + allocatedAddress2);
        memoryManager.printMemoryStatus();

        // 释放内存
        memoryManager.deallocateMemory(allocatedAddress1);
        System.out.println("释放内存:");
        memoryManager.printMemoryStatus();






    }
}

