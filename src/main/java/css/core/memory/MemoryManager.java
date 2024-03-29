package css.core.memory;

import css.core.process.ProcessScheduling;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static css.core.process.ProcessScheduling.linkedList;
import static css.out.file.api.toFrontApiList.giveBlockStatus2Front;


@Slf4j


public class MemoryManager {
    private static MemoryBlock[][] memory;
    private static int[][] cleanblock;


    static {
        //用64个块初始化内存，每个块可存储3个字符
        memory = new MemoryBlock[8][8];
        cleanblock = new int[8][8];
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
                    cleanblock[i][j] = processId;
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
    public static void releaseMemory(int processId) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (cleanblock[i][j] == processId) {
                    memory[i][j].setContent("---");
                    cleanblock[i][j] = -1;
                }
            }
        }
    }

    // ? SK 暂时停止调用展示
    public static int displayMemory() {
        int status = 0;

        //显示内存的当前状态
//        System.out.println("Memory Status:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
//                System.out.print(memory[i][j].getContent() + " ");
                if (memory[i][j].getContent().equals("---")) {
                    status++;
                }
            }
//            System.out.println();

        }

//        System.out.println("空闲空间:"+status);
        return status; //返回空闲空间
    }

    public static int getSystemMemoryUsage() {
        List<Integer> usage = giveBlockStatus2Front();
        List<Integer> temp = new ArrayList<>();
        for (Integer e : usage) {
            if (e == 3) {
                temp.add(e);
            }
        }
        return temp.size();
    }

    public static List<Integer> givememorystatus() {
        List<Integer> greatmemory = new ArrayList<>();
        // 0: 空闲 1:占用 2:正在使用  3: 系统-> DTO FAT


        //? BrainStorm by SK
        //1. 确定方法获取途径... ok

        //2. 2个区段
        //2.1 一区段, 获取数据, 确定对应状态的块数是多少
        //      对应方法: 获取系统占用盘块数( int ) / 获取 .... / .....
        // 一区段负责向二区段提供对应状态占用的盘块数

        //2.2 二区段, 根据已有的对应盘块数, 通过一个for循, 逐个添加对应状态的盘块数
        //      for(i 0 -> 64){
        //          for(System){i -> V; continue}
        //          for(Ready){i -> V; continue}
        //          for(Block){i -> V; continue}
        //          溢出? -> break;报警}
        //      }

        //3. 返回值, 二区段的List<Integer> : K is Item, V is Status {0,1,2}


        //一区段
        List<String> blockList = new ArrayList<>(10); // 阻塞队列
        List<String> readyList = new ArrayList<>(10); // 就绪队列

        linkedList.forEach((k, v) -> {
            if (v.pcb.state == 2) {
                blockList.add(String.valueOf(v.pcb.pcbId));
            } else if (v.pcb.state == 0) {
                readyList.add(String.valueOf(v.pcb.pcbId));
            }
        });

        boolean running_flag = ProcessScheduling.runing != null;

        //正在运行 = 当前指令 = 如果有就+1
        //二区段
        for (int i = 0; i < 64; i++) {

            if (i < getSystemMemoryUsage() * 2) { // 系统模块占用内存 *2
                greatmemory.add(3);
                continue;
            }

            if (i < getSystemMemoryUsage() * 2 + blockList.size() + readyList.size()) { //普通占用内存 占用 = 就绪 + 阻塞
                greatmemory.add(1);
                continue;
            }

            if (running_flag && i == getSystemMemoryUsage() * 2 + blockList.size() + readyList.size()) { //正在运行
                greatmemory.add(2);
                continue;
            }

            greatmemory.add(0);

        }

     /*   int systemBlock = 2 * getSystemMemoryUsage(); // 设置系统模块盘块占用内存为2 * 盘块数( 2 * 64B = 128B)
        for (int i = 0; i < systemBlock; i++) {
            greatmemory.add(3);
        }

        for (int i = 0; i < 64 - displayMemory() - systemBlock; i++) {
            greatmemory.add(1);
        }

        for (int i = 0; i < displayMemory(); i++) {
            greatmemory.add(0);
//            System.out.println("空闲空间:" + displayMemory());
        }


        if (ProcessScheduling.runing != null) {
            greatmemory.add(2);
        }

*/
        return greatmemory;

    }
}

