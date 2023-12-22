package css.out.file.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
public abstract class ByteUtil {
    /**
     * 自制byte数组合并
     *
     * @param byte_1 byte数组1
     * @param byte_2 byte数组2
     * @return 合并后的byte数组
     * @author SK
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
     * @return byte数组(只有一个元素)
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
     * @param bytes byte数组(只有一个元素)
     * @return 0-127的数字
     */
    public static Integer Byte2Int(byte[] bytes) {
        //刷洗操作: 从后往前查找bytes寻找空格代表数32, 然后把剩下的数字保存到新的byte数组中, 然后执行return逻辑
        int sit = bytes.length; //如果找到一个空格, 就把sit往前移动(-1)

        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] == 32) {
                sit--;
            }
        }
        //使用拷贝工具, 将0-sit的bytes拷贝到新的byte数组中
        byte[] bytes1 = new byte[sit];
        System.arraycopy(bytes, 0, bytes1, 0, sit);


        return Integer.parseInt(new BigInteger(bytes1).toString(2), 2);
    }

    /**
     * 空格分隔的Str对象转byte[]
     *
     * @param str
     * @return
     */
    public static byte[] str2Byte(String str) {
        String[] str_temp = str.split(" "); //将str按照空格分割为String[]
        byte[] bytes_temp = new byte[str_temp.length];//创建一个byte[]

        for (int i = 0; i < str_temp.length; i++) {
            bytes_temp[i] = Byte.parseByte(str_temp[i]);
        }
        return bytes_temp;
    }
}
