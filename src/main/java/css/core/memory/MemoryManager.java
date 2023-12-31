package css.core.memory;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;


@Slf4j
public class MemoryManager {
    private static MemoryBlock[][] memory;

    public MemoryManager() {
        //用64个块初始化内存，每个块可存储3个字符
        this.memory = new MemoryBlock[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                memory[i][j] = new MemoryBlock();
            }
        }
    }

    //分配内存
    public static void allocateMemory(int processId, String data) {
        //查找要分配的连续块
        int consecutiveBlocks = calculateConsecutiveBlocks(data.length());

        // 查找要分配的连续块
        int[] startingBlock = findConsecutiveBlocks(consecutiveBlocks);

        //如果找到连续块，则分配内存
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

            System.out.println("为进程分配的内存 " + processId);
        } else {
            System.out.println("进程的内存分配失败 " + processId);
        }
    }
    private static int calculateConsecutiveBlocks(int dataSize) {
        return dataSize / 3 + (dataSize % 3 == 0 ? 0 : 1);
    }

    //查找连续块
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

