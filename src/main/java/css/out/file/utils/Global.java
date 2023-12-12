package css.out.file.utils;

import java.util.List;

/**
 * 全局变量, 全局信息定义
 */
public class Global {

    //!磁盘DISK

    /**
     * 磁盘文件名 = disk.txt
     */
    public static final String DISK_FILE = "disk.txt";

    /**
     * 磁盘文件路径固定
     */
    public static final String DISK_FILE_PATH = "common/file/disk.txt";

    /**
     * 磁盘大小 = 128
     * 单磁盘, 块数128个盘块
     */
    public static final int DISK_SIZE = 128;

    /**
     * 磁盘块大小 = 64
     * 单磁盘, 每个盘块64字节
     */
    public static final int BLOCK_SIZE = 64;

    /**
     * FAT1 位置 = 0号盘块
     */
    public static final int FAT1_DIR = 0;

    /**
     * FAT2 位置 = 1号盘块
     */
    public static final int FAT2_DIR = 1;

    /**
     * 根目录位置 = 2号盘块
     */
    public static final int ROOT_DIR = 2;

    /**
     * FAT分配表中的Null指针 = 514
     * FAT : List[] 下标代表当前块号, 值就是下一个块号, 514代表next = Null
     */
    public static final int Null_Pointer = 514;


    //!目录DIR
    /**
     * 目录标识 = 1
     */
    public static final int DIR_SIGNAL = 1;


    /**
     * 根目录名
     * <p>
     * |  home 用户子目录
     * |  app 应用程序目录
     * |  tmp 临时可变目录
     * |  conf 配置文件目录
     * |  mnt 设备挂载目录
     * |  bin 可执行命令目录
     * |  lib 系统库文件以及系统资料目录
     * |  boot 系统内核程序目录
     * </p>
     */
    public static final String[] ROOT_DIR_NAMES = {"home", "app", "tmp", "conf", "mnt", "bin", "lib", "boot"};


    //!文件FILE

    /**
     * 文件标识 = 0
     */
    public static final int FILE_SIGNAL = 0;

    /**
     * 一次输入的最大字符数 = 255
     * 代表一个字节的容量
     * 输入长度超过时自动分配空间
     */
    public static final int MAX_INPUT_ONCE = 255;

    /**
     * 文件类型 = txt...
     */
    public static final List<String> FILE_TYPE = List.of("txt", "docx");


    //! else

    /**
     * 用户命令接口
     */
    public static final String[] CMD_NAMES = {"create", "copy", "delete", "move", "type", "change", "mkdir", "chdir", "deldir"};

}
