package css.out.file.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static css.out.file.entity.GlobalField.*;
import static css.out.file.handle.HandleBlock.*;
import static css.out.file.handle.HandleDISK.writeDISK;

/**
 * 磁盘
 */
@Data
@Slf4j
public class disk {

    /**
     * 磁盘名
     */
    public String name;

    /**
     * FAT1文件分配表1
     * <p>位示图(下标位置) + 显示链接的指针(值)</p>
     * <p>下标代表当前块号, 值就是下一个块号</p>
     * <p>空值->Null_Pointer</p>
     */
    public List<Integer> FAT1;

    /**
     * FAT2文件分配表2
     * <p>位示图(下标位置) + 显示链接的指针(值)</p>
     * <p>空值->Null_Pointer</p>
     */
    public List<Integer> FAT2;

    /**
     * 一维磁盘块阵列
     * <p>->DISK_SIZE</p>
     */
    public List<block> BLOCKS;


    /**
     * 磁盘限定构造
     * <p>只允许初始化构造, 不允许自定义</p>
     */
    public disk() {
        this.name = DISK_NAME;
        this.BLOCKS = getDefaultBLOCKS(); //获得磁盘空间

        this.FAT1 = getDefaultFAT1(); //获得FAT1对象
        Byte[] FAT1_Byte = getFATBytes(this.FAT1); //获得FAT1字节对象
        mountFAT(this.BLOCKS, FAT1_Byte, 1); //挂载FAT1字节对象

        this.FAT2 = getDefaultFAT2(); //获得FAT2对象
        Byte[] FAT2_Byte = getFATBytes(this.FAT1); //获得FAT2字节对象
        mountFAT(this.BLOCKS, FAT2_Byte, 2); //挂载FAT2字节对象

        writeDISK(this.BLOCKS, WORKSHOP_PATH + DISK_FILE); //写入磁盘
        log.info("{}初始化完成!", this.name);
    }


    // 定义磁盘的读取一行方法 FIXME
//    public String readLine(FileInputStream fis) throws IOException {
//        // 创建一个字节缓冲区
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        // 定义一个字节变量
//        int b;
//        // 循环读取一个字节，直到遇到换行符或文件结束
//        while ((b = fis.read()) != -1 && b != '\n') {
//            // 将字节写入缓冲区
//            baos.write(b);
//        }
//        // 将缓冲区的内容转换为字符串
//        String line = baos.toString();
//        // 关闭缓冲区
//        baos.close();
//        // 返回字符串
//        return line;
//    }

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
