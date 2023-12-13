package css.out.file.entity;

import static css.out.file.utils.GlobalField.BLOCK_SIZE;

/**
 * 字节构成的单个磁盘块
 */
public class block {

    /**
     * 一个磁盘块的字节数组
     */
    public Byte[] bytes;

    /**
     * 新磁盘块构造
     */
    public block() {
        this.bytes = new Byte[BLOCK_SIZE];
        //将每一项都初始化为0 FIXME
        for (int i = 0; i < BLOCK_SIZE; i++) {
            this.bytes[i] = Byte.valueOf("0");
        }
    }

    public block(Byte[] blockBytes) {
        this.bytes = blockBytes;
    }

    public Byte getBlockByteStream() {
        //将这个磁盘块的字节数组转换为字节对象, 字节对象之间用空格隔开
        Byte byteStream = null;

        for (int i = 0; i < BLOCK_SIZE; i++) {
            byteStream = Byte.valueOf(this.bytes[i].toString() + " ");
        }

        return byteStream;
    }

    //将字节对象转换为字节数组bytes
    public Byte[] getBlockByteBuilder(Byte byteStream) {

        Byte[] bytes = new Byte[BLOCK_SIZE];

        String[] byteStreamArray = byteStream.toString().split(" ");
        for (int i = 0; i < BLOCK_SIZE; i++) {
            bytes[i] = Byte.valueOf(byteStreamArray[i]);
        }

        return bytes;
    }
}
