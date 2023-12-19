package css.out.file.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Arrays;

@Slf4j
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

    /**
     * 自制0-127的Integer转单元素Byte[]
     *
     * @param num 0-127的数字
     * @return Byte数组(只有一个元素)
     */
    public static byte[] Int2Byte(Integer num) {//这种数字只能适用于0-127的数字, 需要加上判断
        if (num < 0 || num > 127) {
            log.warn("数字{}溢出无法转换", num);
            //只能返回最大值
            return new byte[]{(byte) 127};
        }
        return new BigInteger(Integer.toBinaryString(num), 2).toByteArray();
    }

    /**
     * 自制单元素Byte[]转0-127的Integer
     *
     * @param Bytes Byte数组(只有一个元素)
     * @return 0-127的数字
     */
    public static Integer Byte2Int(byte[] Bytes) {
        return Integer.parseInt(Arrays.toString(Bytes).replace("[", "").replace("]", ""));
    }
}
