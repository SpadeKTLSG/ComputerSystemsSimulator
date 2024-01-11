package css.core.memory;

import lombok.extern.slf4j.Slf4j;



@Slf4j


public class MemoryManager {
    private static MemoryBlock[][] memory;
    private static int[][] cleanblock;


    public MemoryManager() {
        //用64个块初始化内存，每个块可存储3个字符
        this.memory = new MemoryBlock[8][8];
        this.cleanblock = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                memory[i][j] = new MemoryBlock();
                cleanblock[i][j] = -1;
            }


        }
    }

    //分配内存
    public static void allocateMemory(int processId, String data) {
        // 查找要分配的连续块
        int consecutiveBlocks = data.length() / 3 + (data.length() % 3 == 0 ? 0 : 1);
        int[] startingBlock = findConsecutiveBlocks(consecutiveBlocks);

        // 如果找到连续块，则分配内存
        if (startingBlock != null) {
            MemoryBlock[] allocatedBlocks = new MemoryBlock[consecutiveBlocks];
            int blockIndex = 0;
            for (int i = startingBlock[0]; i < startingBlock[0] + consecutiveBlocks; i++) {
                for (int j = startingBlock[1]; j < 8; j++) {

                    if (blockIndex < data.length()) {
                        int blockSize = Math.min(3, data.length() - blockIndex);
                        memory[i][j].setContent(data.substring(blockIndex, blockIndex + blockSize));
                        allocatedBlocks[blockIndex] = memory[i][j];
                        blockIndex += blockSize;
                    }
                    //跟踪内存被哪些进程所占用
                    cleanblock[i][j]=processId;
                }
            }

            System.out.println("为进程分配的内存 " + processId);
        } else {
            System.out.println("进程的内存分配失败 " + processId);
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

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cleanblock[i][j] == processId) {
                    memory[i][j].setContent("---");
                    cleanblock[i][j] = -1;
                }
            }
        }
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

