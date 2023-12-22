package css.out.file.handleB;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static css.out.file.entiset.GF.*;

/**
 * I级 磁盘(TXT)管理工具类
 */
@Slf4j
public abstract class HandleDISK {

    //! 1. 磁盘 - CRUD


    //! 2. FAT - CRUD


    /**
     * 挂载FAT
     * <p>FATByte 封装到 FATblock对象(单个磁盘块)</p>
     * <p>FATBlock对象 封装到 BLOCKS(磁盘块阵列)</p>
     *
     * @param BLOCKS   磁盘块阵列
     * @param FAT_Byte FAT字节对象
     * @param type     FAT类型
     */
    public static void mountFAT(List<block> BLOCKS, byte[] FAT_Byte, Integer type) {
        switch (type) {
            case 1 -> { //FAT 1
                BLOCKS.set(FAT1_DIR, new block(FAT_Byte));
            }
            case 2 -> { //FAT 2
                BLOCKS.set(FAT2_DIR, new block(FAT_Byte));
            }
            default -> {
                log.warn("FAT类型错误!");
            }
        }
    }


    /**
     * 获取空FAT1
     *
     * @return 默认FAT1
     */
    public static List<Integer> getVoidFAT1() {

        List<Integer> FAT = getVoidFAT2();

        FAT.set(0, 1); //0号块指向1号块FAT1
        FAT.set(1, 2); //1号块指向2号块FAT2
        FAT.set(2, 3); //2号块指向3号块(第一个空闲块)
        return FAT;
    }


    /**
     * 获取空FAT2(全空)
     *
     * @return 默认FAT2
     */
    public static List<Integer> getVoidFAT2() {

        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为0
            FAT.add(Null_Pointer);
        }

        return FAT;
    }


    /**
     * FAT(List) -> Bytes
     *
     * @param fat FAT对象
     * @return FAT字节对象
     */
    public static byte[] FAT2Bytes(List<Integer> fat) {

        byte[] FATByte = new byte[FAT_SIZE];
        for (int i = 0; i < FAT_SIZE; i++) {
            if (Objects.equals(fat.get(i), Null_Pointer)) { //如果这一项是空的, 就在磁盘上写0(空)
                FATByte[i] = 0;
                continue;
            }
            FATByte[i] = fat.get(i).byteValue(); //将FAT列表中每一项的值转换为字节
        }

        return FATByte;
    }


    /**
     * Bytes -> FAT(List)
     *
     * @param bytes FAT字节对象
     * @return FAT对象
     */
    public static List<Integer> Bytes2FAT(byte[] bytes) {
        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为514
            FAT.add(Null_Pointer);
        }

        for (int i = 0; i < FAT_SIZE; i++) {
            if ((int) bytes[i] != 0) {
                FAT.set(i, (int) bytes[i]);
            }
        }

        return FAT;
    }


}
