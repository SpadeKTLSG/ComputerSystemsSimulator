package css.out.file.entity;

import java.util.Arrays;

import static css.out.file.entity.GF.BLOCK_SIZE;

/**
 * 字节构成的单个磁盘块
 */
public class block {

    /**
     * 一个磁盘块的字节数组
     */
    public byte[] bytes;

    /**
     * 新磁盘块构造
     */
    public block() {
        this.bytes = new byte[BLOCK_SIZE];
        //将每一个字节项都初始化为0
        for (int i = 0; i < BLOCK_SIZE; i++) {
            this.bytes[i] = Byte.valueOf("0");
        }
    }

    /**
     * 从字节数组直接构造磁盘块
     *
     * @param blockBytes 字节数组
     */
    public block(byte[] blockBytes) {
        this.bytes = blockBytes;
    }

    @Override
    public String toString() {
        return "\nblock{" +
                "bytes=" + Arrays.toString(bytes) +
                "}";
    }
}
