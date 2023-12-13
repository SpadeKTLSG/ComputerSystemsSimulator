package css.out.file.utils;

import java.util.List;
import java.util.Map;

/**
 * 全局变量&信息定义
 */
public class GlobalField {

    //!磁盘DISK

    /**
     * 默认磁盘名 = 西数8KB磁盘
     */
    public static final String DISK_NAME = "西数8KB磁盘";

    /**
     * 模拟磁盘文件名 = disk.txt
     */
    public static final String DISK_FILE = "disk.txt";

    /**
     * 模拟磁盘文件路径固定
     */
    public static final String DISK_FILE_PATH = "common/file/disk.txt";

    /**
     * 磁盘大小 = 128
     * <p>单磁盘, 块数128个盘块</p>
     */
    public static final int DISK_SIZE = 128;

    /**
     * 磁盘块大小 = 64
     * <p>单磁盘, 每个盘块64字节</p>
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
     * FAT 大小  = BLOCK_SIZE
     */
    public static final int FAT_SIZE = BLOCK_SIZE;

    /**
     * 根目录位置 = 2号盘块
     */
    public static final int ROOT_DIR = 2;

    /**
     * FAT分配表中的Null指针 = 514
     */
    public static final int Null_Pointer = 514;


    //!目录DIR

    /**
     * FCB占用的字节数 = 8B
     */
    public static int FCB_BYTE_LENGTH = 8;

    /**
     * FCB中每个字段的字节数
     * <p>目录路径 3</p>
     * <p>磁盘路径 1</p>
     * <p>扩展名 2</p>
     * <p>类型标识 1</p>
     * <p>长度 1</p>
     */
    public static Map<String, Integer> FCB_BYTE_LENGTH_MAP = Map.of(
            "pathName", 3,
            "startBlock", 1,
            "extendName", 2,
            "typeFlag", 1,
            "fileLength", 1
    );


    /**
     * 目录标识 = 1
     */
    public static final int DIR_SIGNAL = 1;


    /**
     * 根目录名
     * <p>|  home 用户子目录</p>
     * <p>|  app 应用程序目录</p>
     * <p>|  tmp 临时可变目录</p>
     * <p>|  conf 配置文件目录</p>
     * <p>|  mnt 设备挂载目录</p>
     * <p>|  bin 可执行命令目录</p>
     * <p>|  lib 系统库文件以及系统资料目录</p>
     * <p>|  boot 系统内核程序目录</p>
     */
    public static final String[] ROOT_DIR_NAMES = {"home", "app", "tmp", "conf", "mnt", "bin", "lib", "boot"};

    //文件夹没有类型

    /**
     * 默认的文件夹类型为不存在
     */
    public static final String EMPTY_DIR_TYPE = "";

    /**
     * 默认的文件夹名 = 新文件夹
     */
    public static final String DEFAULT_DIR_NAME = "新文件夹";

    /**
     * 默认/初始化的文件夹长度 = 0
     */
    public static final int DEFAULT_DIR_LENGTH = 0;

    //!文件FILE

    /**
     * 文件标识 = 0
     */
    public static final int FILE_SIGNAL = 0;

    /**
     * 一次输入的最大字符数 = 255/一个字节容量
     * <p>输入长度超过时自动分配空间</p>
     */
    public static final int MAX_INPUT_ONCE = 255;

    /**
     * 文件类型 = .txt, .exe...
     */
    public static final List<String> FILE_TYPE = List.of(".txt", ".docx");

    /**
     * 默认的文件类型为"." 区别于文件夹代表空
     */
    public static final String EMPTY_FILE_TYPE = ".";

    /**
     * 默认的文件名 = 新文件夹
     */
    public static final String DEFAULT_FILE_NAME = "新文件";

    /**
     * 默认/初始化的文件长度 = 0
     */
    public static final int DEFAULT_FILE_LENGTH = 0;


}
