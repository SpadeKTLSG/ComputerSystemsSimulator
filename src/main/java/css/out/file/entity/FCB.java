package css.out.file.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.utils.GetBlock.Find_diskNewFreeBlock_of;
import static css.out.file.utils.GlobalField.*;

/**
 * FCB 文件控制块
 * 8B绑定文件/目录HEAD, 用于描述文件的基本信息
 */
@Slf4j
@Data
public class FCB {


    /**
     * 目录名+文件名, 通过/分割
     * 通过此字段可以从/(根目录)唯一查找到该文件
     * -> ROOT_DIR_NAMES/.../FILE_NAME
     */
    public String pathName; //TODO 通过工具类来获取路径

    /**
     * 扩展名(目录为空)
     * -> FILE_TYPE
     */
    public String ext; //TODO 通过工具类来获取

    /**
     * 文件目录标识
     * -> FILE_SIGNAL:0 / DIR_SIGNAL:1
     */
    public int flag;

    /**
     * 起始盘块号
     * 需要进行磁盘分配
     */
    public int start;

    /**
     * 文件长度(目录为空)
     * 单位:字节
     */
    public int length;

    /**
     * 一般全量构造
     *
     * @param pathName 目录名+文件名
     * @param ext      扩展名
     * @param flag     文件目录标识
     * @param start    起始盘块号
     * @param length   文件长度
     */
    public FCB(String pathName, String ext, int flag, int start, int length) {
        this.pathName = pathName;
        this.ext = ext;
        this.flag = flag;
        this.start = start;
        this.length = length;
    }


    /**
     * 在某处(指定目录和磁盘块)默认快速新建文件/文件夹构造
     *
     * @param start 起始盘块号
     */
    public FCB(String pathName, int start, int flag) {
        if (flag == DIR_SIGNAL) {
            this.pathName = pathName;
            this.ext = EMPTY_DIR_TYPE;
            this.flag = FILE_SIGNAL;
            this.start = start;
            this.length = FCB_BYTE_LENGTH;

        } else if (flag == FILE_SIGNAL) {

            this.pathName = "新建文件";
            this.ext = FILE_TYPE.get(0);
            this.flag = FILE_SIGNAL;
            this.start = start;
            this.length = FCB_BYTE_LENGTH;

        } else {
            //TODO 提示用户异常信息
            log.info("FCB构造失败, flag错误");
        }

    }


    /**
     * 临时默认快速新建文件构造
     * 默认放在TMP目录下new一个文件
     */
    public FCB() {
        this.pathName = "新建文件";
        this.ext = FILE_TYPE.get(0);
        this.flag = FILE_SIGNAL;
        this.start = Find_diskNewFreeBlock_of(ROOT.tmp); //TODO
        this.length = FCB_BYTE_LENGTH;
    }


    // 定义文件控制块的字节数组转换方法
    public byte[] toBytes() {
        //TODO 自己模拟
        // 创建一个8字节的字节数组
        byte[] bytes = new byte[8];
        // 将文件名+目录名转换为3字节
        byte[] nameBytes = pathName.getBytes();
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

        return bytes;
    }

    // 定义文件控制块的字节数组还原方法
    public FCB fromBytes(byte[] bytes) {
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

