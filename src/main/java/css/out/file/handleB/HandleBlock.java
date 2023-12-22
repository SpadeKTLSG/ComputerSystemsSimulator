package css.out.file.handleB;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;

/**
 * II级 磁盘块工具类
 */
@Slf4j
public abstract class HandleBlock {


    public static int getFreeBlock() {
        //TODO: 根据FAT从磁盘块中找到空闲的磁盘块位置
        //在全局的FAT中查找第一个值为514的位置, 同时将FAT最晚的一个块指向这个位置, 这个位置同样指向空(514)
        //返回这个位置的块号

        //对象: diskSyS.disk.FAT1 + FAT2, 从开始盘块:全局变量FAT1开始指针查找, 将对应位置的值作为下一个访问的位置, 直到读取到514代表空; 而后写入新的对象
        //如果FAT1遍历完了, 仍然没有找到空闲块, 则去FAT2查找, 仍然没有找到就报错
        int pos = FAT1_DIR; //从FAT1开始
        for (int i = 0; i < FAT_SIZE; i++) {
            if (diskSyS.disk.FAT1.get(pos) == Null_Pointer) {
                diskSyS.disk.FAT1.set(pos, Null_Pointer);
                diskSyS.disk.FAT2.set(pos, Null_Pointer);
                return pos;
            }
            pos = diskSyS.disk.FAT1.get(pos);
        }

        return 114;
    }


    /**
     * 释放盘块空间: 设置为空
     *
     * @param blockNum
     */
    public static void setBlockFree(int blockNum) {
        //TODO 测试能否直接覆盖输入
        //FIXME
    }

    /**
     * 挂载FAT
     * <p>FATByte 封装到 FATblock对象(单个磁盘块)</p>
     * <p>FATBlock对象 封装到 BLOCKS(磁盘块阵列)</p>
     *
     * @param BLOCKS   磁盘块阵列
     * @param FAT_Byte FAT字节对象
     * @param type     FAT类型
     */
    public static void mountFAT(List<block> BLOCKS, byte[] FAT_Byte, Integer type) {
        switch (type) {
            case 1 -> { //FAT 1
                BLOCKS.set(0, new block(FAT_Byte));
            }
            case 2 -> { //FAT 2
                BLOCKS.set(1, new block(FAT_Byte));
            }
            default -> {
                log.warn("FAT类型错误!");
            }
        }
    }

    /**
     * 获取默认FAT1
     *
     * @return 默认FAT1
     */
    public static List<Integer> getDefaultFAT1() {

        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为指向空值
            FAT.add(Null_Pointer);
        }

        FAT.set(0, 1); //0号块指向1号块FAT1
        FAT.set(1, 2); //1号块指向2号块FAT2
        FAT.set(2, 3); //2号块指向3号块空闲块
        return FAT;
    }

    /**
     * 获取默认FAT2
     *
     * @return 默认FAT2
     */
    public static List<Integer> getDefaultFAT2() {

        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为0
            FAT.add(Null_Pointer);
        }

        return FAT;
    }

    /**
     * 获取FAT字节对象
     * <p>List -> Bytes</p>
     *
     * @param fat FAT对象
     * @return FAT字节对象
     */
    public static byte[] getFATBytes(List<Integer> fat) {

        byte[] FATByte = new byte[FAT_SIZE];
        for (int i = 0; i < FAT_SIZE; i++) {
            if (Objects.equals(fat.get(i), Null_Pointer)) { //如果这一项是空的, 就在磁盘上写0(空)
                FATByte[i] = 0;
                continue;
            }
            FATByte[i] = fat.get(i).byteValue(); //将FAT列表中每一项的值转换为字节
        }

        return FATByte;
    }

    /**
     * 获取FAT对象
     * <p>List <- Bytes</p>
     *
     * @param bytes FAT字节对象
     * @return FAT对象
     */
    public static List<Integer> fromFATBytes(byte[] bytes) {
        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为514
            FAT.add(Null_Pointer);
        }

        for (int i = 0; i < FAT_SIZE; i++) {
            if ((int) bytes[i] != 0) {
                FAT.set(i, (int) bytes[i]);
            }
        }

        return FAT;
    }


    /**
     * 获取默认格式化的BLOCKS
     *
     * @return 默认BLOCKS
     */
    public static List<block> getDefaultBLOCKS() {

        List<block> BLOCKS = new ArrayList<>(DISK_SIZE);
        for (int i = 0; i < DISK_SIZE; i++) {  //初始化赋值全部为block
            BLOCKS.add(new block());
        }

        return BLOCKS;
    }

    /**
     * 字节数组设置BLOCKS的单个block内容
     *
     * @param bytes    字节数组内容
     * @param blockNum 块号
     */
    public static void setSingleBLOCKS(byte[] bytes, int blockNum) {
        diskSyS.disk.BLOCKS.set(blockNum, new block(bytes));
    }

    /**
     * 磁盘的读取一行(一个block)
     *
     * @param path 磁盘TXT映射文件路径
     * @param pos  行号(位置)
     * @return String化的内容对象(还是byte)
     */
    public String read1Block(String path, Integer pos) throws IOException {

        File diskFile = new File(path);
        String res = "";
        if (!diskFile.exists()) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                log.error("创建磁盘映射文件失败, 错误日志: {}", e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(WORKSHOP_PATH + DISK_FILE), StandardCharsets.UTF_8))) {
            //跳过前面的pos-1行, 读取pos行的内容到s
            for (int i = 0; i < pos - 1; i++) {
                br.readLine();
            }
            res = br.readLine();

        } catch (Exception e) {
            log.error("读取磁盘映射文件{}失败, 错误日志: {}", path, e.getMessage());
        }

        return res;
    }


    // 定义磁盘的分配空闲块方法 FIXME
//    public int allocBlock() {
//        // 遍历位示图
//        for (int i = 0; i < DISK_SIZE; i++) {
//            // 如果找到一个空闲块
//            if (bitmap[i] == 0) {
//                // 设置位示图为已分配
//                bitmap[i] = 1;
//                // 设置文件分配表为结束标志
//                fat[0].put(i, Null_Pointer);
//                fat[1].put(i, Null_Pointer);
//                // 返回空闲块号
//                return i;
//            }
//        }
//        // 如果没有找到空闲块，返回-1
//        return -1;
//    }

    // 定义磁盘的释放块方法
//    public void freeBlock(int block) {
//        // 检查块号是否合法
//        // TODO generate more method for safety
//        if (block < 0 || block >= DISK_SIZE) {
//            return;
//        }
//        // 设置位示图为未分配
//        bitmap[block] = 0;
//        // 设置文件分配表为0
//        fat[0].put(block, 0);
//        fat[1].put(block, 0);
//        // 清空磁盘块内容
//        Arrays.fill(blocks[block], (byte) 0);
//    }

    // 定义磁盘的读取文件内容方法
//    public String readFile(int start) {
//        // 创建一个字符串缓冲区
//        StringBuilder sb = new StringBuilder();
//        // 定义一个当前块变量
//        int current = start;
//        // 循环读取文件内容，直到遇到结束标志
//        while (current != Null_Pointer) {
//            // 将当前块的内容转换为字符串
//            String content = new String(blocks[current]);
//            // 将字符串追加到缓冲区
//            sb.append(content);
//            // 获取下一个块号
//            current = fat[0].get(current);
//        }
//        // 将缓冲区的内容转换为字符串
//        String file = sb.toString();
//        // 返回字符串
//        return file;
//    }

    // 定义磁盘的写入文件内容方法
//    public void writeFile(int start, String file) {
//        // 定义一个当前块变量
//        int current = start;
//        // 定义一个文件长度变量
//        int len = file.length();
//        // 定义一个文件指针变量
//        int pos = 0;
//        // 循环写入文件内容，直到文件结束
//        while (pos < len) {
//            // 如果当前块已满
//            if (pos % BLOCK_SIZE == 0 && pos > 0) {
//                // 分配一个新的空闲块
//                int next = allocBlock();
//                // 如果没有空闲块，抛出异常 TODO 通知系统核心模块处理, 创建一个问题处理进程
//                if (next == -1) {
//                    throw new RuntimeException("Disk is full");
//                }
//                // 设置文件分配表的指针
//                fat[0].put(current, next);
//                fat[1].put(current, next);
//                // 更新当前块
//                current = next;
//            }
//            // 将文件内容的一个字节写入当前块
//            blocks[current][pos % BLOCK_SIZE] = (byte) file.charAt(pos);
//            // 更新文件指针
//            pos++;
//        }
//    }

}
