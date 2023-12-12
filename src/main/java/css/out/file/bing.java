package css.out.file;

// 这是一个简单的Java程序的模拟设计，仅供参考，可能存在错误或不足
// 请在合适的环境中运行和测试

// 导入必要的包

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// 定义一个文件系统类
public class bing {
    // 定义一些常量
    public static final int BLOCK_SIZE = 64; // 块大小
    public static final int DISK_SIZE = 128; // 磁盘大小
    public static final int ROOT_DIR = 2; // 根目录块号
    public static final int END = 514; // 结束标志
    public static final String DISK_FILE = "disk.txt"; // 磁盘文件名
    public static final String[] DIR_NAMES = {"home", "app", "tmp", "conf", "mnt", "bin", "lib", "boot"}; // 根子目录名
    public static final String[] CMD_NAMES = {"create", "copy", "delete", "move", "type", "change", "mkdir", "chdir", "deldir"}; // 命令名
    
    // 定义一个文件控制块类
    public static class FCB {
        // 定义文件控制块的属性
        public String name; // 文件名+目录名
        public String ext; // 扩展名
        public int flag; // 文件目录标识
        public int start; // 起始盘块号
        public int length; // 文件长度

        // 定义文件控制块的构造方法
        public FCB(String name, String ext, int flag, int start, int length) {
            this.name = name;
            this.ext = ext;
            this.flag = flag;
            this.start = start;
            this.length = length;
        }

        // 定义文件控制块的字节数组转换方法
        public byte[] toBytes() {
            // 创建一个8字节的字节数组
            byte[] bytes = new byte[8];
            // 将文件名+目录名转换为3字节
            byte[] nameBytes = name.getBytes();
            System.arraycopy(nameBytes, 0, bytes, 0, 3);
            // 将扩展名转换为2字节
            byte[] extBytes = ext.getBytes();
            System.arraycopy(extBytes, 0, bytes, 3, 2);
            // 将文件目录标识转换为1字节
            bytes[5] = (byte) flag;
            // 将起始盘块号转换为1字节
            bytes[6] = (byte) start;
            // 将文件长度转换为1字节
            bytes[7] = (byte) length;
            // 返回字节数组
            return bytes;
        }

        // 定义文件控制块的字节数组还原方法
        public static FCB fromBytes(byte[] bytes) {
            // 检查字节数组长度是否为8
            if (bytes.length != 8) {
                return null;
            }
            // 从字节数组中还原文件控制块的属性
            String name = new String(bytes, 0, 3);
            String ext = new String(bytes, 3, 2);
            int flag = bytes[5];
            int start = bytes[6];
            int length = bytes[7];
            // 创建一个文件控制块对象
            FCB fcb = new FCB(name, ext, flag, start, length);
            // 返回文件控制块对象
            return fcb;
        }
    }

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
            fat = new Map[2];
            fat[0] = new HashMap<>();
            fat[1] = new HashMap<>();
            // 初始化磁盘块
            blocks = new byte[DISK_SIZE][BLOCK_SIZE];
        }

        // 定义磁盘的加载方法
        public void load() throws IOException {
            // 创建一个文件对象
            File file = new File(DISK_FILE);
            // 检查文件是否存在
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
                // 读取文件分配表
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
                // 读取磁盘块
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
            // 设置位示图的第0,1,2块为已分配
            bitmap[0] = 1;
            bitmap[1] = 1;
            bitmap[2] = 1;
            // 设置文件分配表的第0,1,2项为结束标志
            fat[0].put(0, END);
            fat[0].put(1, END);
            fat[0].put(2, END);
            fat[1].put(0, END);
            fat[1].put(1, END);
            fat[1].put(2, END);
            // 创建根子目录的文件控制块
            for (int i = 0; i < DIR_NAMES.length; i++) {
                // 创建一个文件控制块对象
                FCB fcb = new FCB(DIR_NAMES[i], "", 1, 0, 0);
                // 将文件控制块转换为字节数组
                byte[] bytes = fcb.toBytes();
                // 将字节数组复制到磁盘块的第2块
                System.arraycopy(bytes, 0, blocks[2], i * 8, 8);
            }
        }

        // 定义磁盘的读取一行方法
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

        // 定义磁盘的分配空闲块方法
        public int allocBlock() {
            // 遍历位示图
            for (int i = 0; i < DISK_SIZE; i++) {
                // 如果找到一个空闲块
                if (bitmap[i] == 0) {
                    // 设置位示图为已分配
                    bitmap[i] = 1;
                    // 设置文件分配表为结束标志
                    fat[0].put(i, END);
                    fat[1].put(i, END);
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
            while (current != END) {
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
                    // 如果没有空闲块，抛出异常
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
