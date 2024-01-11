package css.core.memory;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


@Slf4j




public class MemoryManager {
    private static MemoryBlock[][] memory;
    public  Map<Integer, MemoryBlock[]> threadMemoryMap; // 用于跟踪线程所使用的内存块

    public MemoryManager() {
        //用64个块初始化内存，每个块可存储3个字符
        this.memory = new MemoryBlock[8][8];
        this.threadMemoryMap = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                memory[i][j] = new MemoryBlock();
            }


        }
    }

    //分配内存
    public static void allocateMemory(int processId, String data) {
        // Find consecutive blocks for allocation
        int consecutiveBlocks = data.length() / 3 + (data.length() % 3 == 0 ? 0 : 1);
        int[] startingBlock = findConsecutiveBlocks(consecutiveBlocks);

        // Allocate memory if consecutive blocks are found
        if (startingBlock != null) {
            int blockIndex = 0;
            for (int i = startingBlock[0]; i < startingBlock[0] + consecutiveBlocks; i++) {
                for (int j = startingBlock[1]; j < 8; j++) {
                    if (blockIndex < data.length()) {
                        memory[i][j].setContent(data.substring(blockIndex, blockIndex + 3));
                        blockIndex += 3;
                    }
                }
            }

            System.out.println("Memory allocated for Process " + processId);
        } else {
            System.out.println("Memory allocation failed for Process " + processId);
        }
    }

    private static int[] findConsecutiveBlocks(int consecutiveBlocks) {

        //查找并返回连续分配的起始块索引
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= 8 - consecutiveBlocks; j++) {
                if (isConsecutiveBlocksAvailable(i, j, consecutiveBlocks)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static boolean isConsecutiveBlocksAvailable(int row, int col, int consecutiveBlocks) {
        //检查从给定索引开始的连续块是否可用
        for (int j = col; j < col + consecutiveBlocks; j++) {
            if (!memory[row][j].getContent().equals("---")) {
                return false;
            }
        }
        return true;


    }


    // 线程结束时清理内存
    public void releaseMemory(int processId) {
        MemoryBlock[] blocksUsedByThread = threadMemoryMap.get(processId);
        if (blocksUsedByThread != null) {
            for (MemoryBlock block : blocksUsedByThread) {
                if (block != null) {
                    block.setContent("---"); // 清空线程使用的内存块
                }

            }
            threadMemoryMap.remove(processId); // 清理线程与内存块的映射关系
            System.out.println("Memory released for Process " + processId);
        } else {
            System.out.println("No memory allocated for Process " + processId);
        }
    }


    private static int calculateConsecutiveBlocks(int dataSize) {

        return dataSize / 3 + (dataSize % 3 == 0 ? 0 : 1);
    }

    //查找连续块


    public static void displayMemory() {
        //显示内存的当前状态
        System.out.println("Memory Status:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(memory[i][j].getContent() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

