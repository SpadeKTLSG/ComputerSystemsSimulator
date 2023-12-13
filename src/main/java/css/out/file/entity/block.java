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
    }

    public block(Byte[] blockBytes) {
        this.bytes = blockBytes;
    }
}
