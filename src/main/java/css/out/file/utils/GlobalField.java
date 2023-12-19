package css.out.file.utils;

import java.util.List;
import java.util.Map;

/**
 * 全局变量&信息定义
 */
public abstract class GlobalField {

    //!磁盘DISK

    /**
     * 默认磁盘名 = 西数8KB磁盘
     */
    public static final String DISK_NAME = "西数8KB暖气片";

    /**
     * 模拟磁盘文件名 = disk.txt
     */
    public static final String DISK_FILE = "disk.txt";

    /**
     * 与外界交互文件路径固定
     */
    public static final String WORKSHOP_PATH = "common/file";

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
    public static final int FAT2_DIR = FAT1_DIR + 1;

    /**
     * FAT 大小  = BLOCK_SIZE
     */
    public static final int FAT_SIZE = BLOCK_SIZE;

    /**
     * FAT分配表中的Null指针 = 514
     */
    public static final int Null_Pointer = 514;

    /**
     * 根目录下所有目录挂载位置 = 2号盘块
     */
    public static final int ROOT_DIR_BLOCK = FAT2_DIR + 1;

    /**
     * 根目录本体挂载位置 = 不存在
     */
    public static final int ROOT_DIR_BLOCK_WHERE = Null_Pointer;


    //!目录DIR

    /**
     * FCB占用的字节数 = 8B
     */
    public static final int FCB_BYTE_LENGTH = 8;

    /**
     * 文件目录树结构的名称
     */
    public static final String FILE_TREE_NAME = "尤克特拉希尔";

    /**
     * FCB中每个字段的字节数
     * <p>目录路径 3</p>
     * <p>磁盘路径 1</p>
     * <p>扩展名 2</p>
     * <p>类型标识 1</p>
     * <p>长度 1</p>
     */
    public static Map<String, Integer> FCB_LENGTH = Map.of(
            "pathName", 3,
            "startBlock", 1,
            "extendName", 2,
            "typeFlag", 1,
            "fileLength", 1
    );


    /**
     * 根目录鉴权标识 = root
     */
    public static final String ROOT_AUTH = "root";


    //?文件夹没有类型

    /**
     * 默认的文件夹扩展名不存在
     */
    public static final String DIR_EXTEND = "";

    /**
     * 默认的文件夹名 = 新文件夹
     */
    public static final String DIR_NAME_DEFAULT = "新文件夹";

    /**
     * 默认/初始化的文件夹长度 = 0
     */
    public static final int DIR_LENGTH_DEFAULT = 0;


    //!文件FILE


    /**
     * 一次输入的最大字符数 = 255/一个字节容量
     * <p>输入长度超过时自动分配空间</p>
     */
    public static final int MAX_INPUT_INONCE = 255;

    /**
     * 文件扩展名S List = .txt,...
     */
    public static final List<String> FILE_EXTEND = List.of(".txt", ".docx", ".md", ".tmp", ".dll", ".java");

    /**
     * 默认的文件类型为"." 区别于文件夹代表空
     */
    public static final String FILE_EXTEND_DEFAULT = ".";

    /**
     * 默认的文件名 = 新文件夹
     */
    public static final String FILE_NAME_DEFAULT = "新文件";

    /**
     * 默认/初始化的文件长度 = 0
     */
    public static final int FILE_LENGTH_DEFAULT = 0;


}
