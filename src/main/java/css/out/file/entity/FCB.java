package css.out.file.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * FCB 文件控制块
 * 8B绑定文件/目录HEAD, 用于描述文件的基本信息
 */
@Data
@AllArgsConstructor
public class FCB {

    /**
     * 目录名+文件名, 通过/分割
     * 通过此字段可以从/(根目录)唯一查找到该文件
     * -> ROOT_DIR_NAMES/.../FILE_NAME
     */
    public String name;

    /**
     * 扩展名
     * -> FILE_TYPE
     */
    public String ext;

    /**
     * 文件目录标识
     * -> FILE_SIGNAL:0 / DIR_SIGNAL:1
     */
    public int flag;
    public int start; // 起始盘块号
    public int length; // 文件长度


    // 定义文件控制块的字节数组转换方法
    public byte[] toBytes() {
        //TODO 自己模拟
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

