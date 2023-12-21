package css.out.file.handle;

import css.out.file.entity.file;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.utils.ByteUtil.Byte2Int;

/**
 * II级 文件/文件夹工具类
 */
@Slf4j
public abstract class HandleFile {

    /**
     * 获得文件内容String类型的长度, 单位:字节
     *
     * @param file 文件
     * @return 文件内容String类型的长度
     */
    public static Integer getFileContextLength(file file) {
        return file.getContent().length();
    }

    /**
     * 根据内容设置文件内容String类型的长度, 单位:字节
     *
     * @param content 文件内容
     * @return 文件内容String类型的长度
     */
    public static Integer setFileContextLength(String content) {
        return content.length();
    }


    /**
     * 将一个指定的字节数组对象转换为固定对应长度的字节数组
     *
     * @param bytes 指定的字节数组
     * @param len   指定的数组长度
     * @return 转换后的字节数组
     */
    public static byte[] toFixedLengthBytes(byte[] bytes, int len) {

        if (bytes.length < len) { // 如果长度小于len，就在右边填充空格
            byte[] padded = new byte[len];
            System.arraycopy(bytes, 0, padded, 0, bytes.length);
            for (int i = bytes.length; i < len; i++) {
                padded[i] = ' ';
            }
            return padded;
        } else if (bytes.length > len) { // 如果compressed的长度大于len，就截取左边的len个字节
            log.warn("有对象被截断");
            byte[] truncated = new byte[len];
            System.arraycopy(bytes, 0, truncated, 0, len);
            return truncated;
        }
        // 如果compressed的长度等于len，就直接返回
        else {
            return bytes;
        }
    }


    /**
     * 将一个固定对应长度的字节数组转换为指定的数组对象
     * <p>有一连串的字节数组的, 将其全部合为一个数字</p>
     */
    public static Integer fromFixedLengthBytes(byte[] bytes, int len) {
        if (len != bytes.length) {
            log.warn("非法的字节数组长度, 对象{}", bytes);
        }

        return Byte2Int(bytes);

    }

}
