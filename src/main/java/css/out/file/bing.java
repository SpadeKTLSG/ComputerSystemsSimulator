package css.out.file;


import css.out.file.entity.FCB;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static css.out.file.utils.Global.*;


public class bing {


    // 定义一个磁盘类
    public static class Disk {
        // 定义磁盘的属性
        public byte[] bitmap; // 位示图
        public Map<Integer, Integer>[] fat; // 文件分配表
        public byte[][] blocks; // 磁盘块

        // 定义磁盘的构造方法
        public Disk() {
            // 初始化位示图
            bitmap = new byte[DISK_SIZE];
            // 初始化文件分配表
            //直接写死两个FAT, FAT1 and FAT2全局变量
            //可以直接用List, 下标就是块号, 值就是下一个块号, 514代表没有
            fat = new Map[2];
            fat[0] = new HashMap<>();
            fat[1] = new HashMap<>();
            // 初始化磁盘块
            //! TODO 这里拉平为一维数组
            blocks = new byte[DISK_SIZE][BLOCK_SIZE];
        }

        // 定义磁盘的加载方法
        public void load() throws IOException {


            // 创建一个文件对象
            File file = new File(DISK_FILE);

            // 检查文件是否存在 TODO 固定存在否则报错磁盘已拔出
            if (!file.exists()) {
                // 如果不存在，创建一个新文件
                file.createNewFile();
                // 初始化根目录
                initRootDir();
                // 保存磁盘到文件
                save();
            } else {
                // 如果存在，读取文件内容
                FileInputStream fis = new FileInputStream(file);

                // 读取位示图
                fis.read(bitmap);

                // 读取文件分配表一行一个键值对实现
                for (int i = 0; i < 2; i++) {
                    // 读取一行键值对
                    String line = readLine(fis);
                    // 按空格分割键值对
                    String[] pairs = line.split(" ");
                    // 遍历键值对
                    for (String pair : pairs) {
                        // 按冒号分割键和值
                        String[] kv = pair.split(":");
                        // 将键和值转换为整数
                        int key = Integer.parseInt(kv[0]);
                        int value = Integer.parseInt(kv[1]);
                        // 将键值对存入文件分配表
                        fat[i].put(key, value);
                    }
                }


                // 读取磁盘块, 无需读取, 需要时候再读取, +线程模拟调入内存
                for (int i = 0; i < DISK_SIZE; i++) {
                    // 读取一个磁盘块
                    fis.read(blocks[i]);
                }
                // 关闭文件输入流
                fis.close();
            }
        }

        // 定义磁盘的保存方法
        public void save() throws IOException {
            // 创建一个文件对象
            File file = new File(DISK_FILE);
            // 创建一个文件输出流
            FileOutputStream fos = new FileOutputStream(file);
            // 写入位示图
            fos.write(bitmap);
            // 写入文件分配表
            for (int i = 0; i < 2; i++) {
                // 遍历文件分配表
                for (Map.Entry<Integer, Integer> entry : fat[i].entrySet()) {
                    // 将键和值转换为字符串
                    String key = String.valueOf(entry.getKey());
                    String value = String.valueOf(entry.getValue());
                    // 将键值对写入文件
                    fos.write((key + ":" + value + " ").getBytes());
                }
                // 写入换行符
                fos.write("\n".getBytes());
            }
            // 写入磁盘块
            for (int i = 0; i < DISK_SIZE; i++) {
                // 写入一个磁盘块
                fos.write(blocks[i]);
            }
            // 关闭文件输出流
            fos.close();
        }

        // 定义磁盘的初始化根目录方法
        public void initRootDir() {
            // !设置位示图的第0,1,2块为已分配 错漏
            bitmap[0] = 1;
            bitmap[1] = 1;
            bitmap[2] = 1;

            // 设置文件分配表的第0,1,2项为结束标志
            fat[0].put(0, Null_Pointer);
            fat[0].put(1, Null_Pointer);
            fat[0].put(2, Null_Pointer);
            fat[1].put(0, Null_Pointer);
            fat[1].put(1, Null_Pointer);
            fat[1].put(2, Null_Pointer);

            // 创建根子目录的文件控制块
            for (int i = 0; i < ROOT_DIR_NAMES.length; i++) {
                // 创建一个文件控制块对象
                FCB fcb = new FCB(ROOT_DIR_NAMES[i], "", 1, 0, 0);
                // 将文件控制块转换为字节数组
                byte[] bytes = fcb.toBytes();
                // 将字节数组复制到磁盘块的第2块
                System.arraycopy(bytes, 0, blocks[2], i * 8, 8);
            }
        }

        // 定义磁盘的读取一行方法 FIXME
        public String readLine(FileInputStream fis) throws IOException {
            // 创建一个字节缓冲区
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 定义一个字节变量
            int b;
            // 循环读取一个字节，直到遇到换行符或文件结束
            while ((b = fis.read()) != -1 && b != '\n') {
                // 将字节写入缓冲区
                baos.write(b);
            }
            // 将缓冲区的内容转换为字符串
            String line = baos.toString();
            // 关闭缓冲区
            baos.close();
            // 返回字符串
            return line;
        }

        // 定义磁盘的分配空闲块方法 FIXME
        public int allocBlock() {
            // 遍历位示图
            for (int i = 0; i < DISK_SIZE; i++) {
                // 如果找到一个空闲块
                if (bitmap[i] == 0) {
                    // 设置位示图为已分配
                    bitmap[i] = 1;
                    // 设置文件分配表为结束标志
                    fat[0].put(i, Null_Pointer);
                    fat[1].put(i, Null_Pointer);
                    // 返回空闲块号
                    return i;
                }
            }
            // 如果没有找到空闲块，返回-1
            return -1;
        }

        // 定义磁盘的释放块方法
        public void freeBlock(int block) {
            // 检查块号是否合法
            // TODO generate more method for safety
            if (block < 0 || block >= DISK_SIZE) {
                return;
            }
            // 设置位示图为未分配
            bitmap[block] = 0;
            // 设置文件分配表为0
            fat[0].put(block, 0);
            fat[1].put(block, 0);
            // 清空磁盘块内容
            Arrays.fill(blocks[block], (byte) 0);
        }

        // 定义磁盘的读取文件内容方法
        public String readFile(int start) {
            // 创建一个字符串缓冲区
            StringBuilder sb = new StringBuilder();
            // 定义一个当前块变量
            int current = start;
            // 循环读取文件内容，直到遇到结束标志
            while (current != Null_Pointer) {
                // 将当前块的内容转换为字符串
                String content = new String(blocks[current]);
                // 将字符串追加到缓冲区
                sb.append(content);
                // 获取下一个块号
                current = fat[0].get(current);
            }
            // 将缓冲区的内容转换为字符串
            String file = sb.toString();
            // 返回字符串
            return file;
        }

        // 定义磁盘的写入文件内容方法
        public void writeFile(int start, String file) {
            // 定义一个当前块变量
            int current = start;
            // 定义一个文件长度变量
            int len = file.length();
            // 定义一个文件指针变量
            int pos = 0;
            // 循环写入文件内容，直到文件结束
            while (pos < len) {
                // 如果当前块已满
                if (pos % BLOCK_SIZE == 0 && pos > 0) {
                    // 分配一个新的空闲块
                    int next = allocBlock();
                    // 如果没有空闲块，抛出异常 TODO 通知系统核心模块处理, 创建一个问题处理进程
                    if (next == -1) {
                        throw new RuntimeException("Disk is full");
                    }
                    // 设置文件分配表的指针
                    fat[0].put(current, next);
                    fat[1].put(current, next);
                    // 更新当前块
                    current = next;
                }
                // 将文件内容的一个字节写入当前块
                blocks[current][pos % BLOCK_SIZE] = (byte) file.charAt(pos);
                // 更新文件指针
                pos++;
            }
        }
    }
}
