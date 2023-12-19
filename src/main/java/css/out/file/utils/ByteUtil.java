package css.out.file.utils;

public abstract class ByteUtil {
    /**
     * 自制byte数组合并
     * @author SK
     *
     * @param byte_1 byte数组1
     * @param byte_2 byte数组2
     * @return 合并后的byte数组
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
