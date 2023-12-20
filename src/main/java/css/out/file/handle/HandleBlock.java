package css.out.file.handle;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static css.out.file.entity.GlobalField.*;

/**
 * 磁盘块相关工具类
 */
@Slf4j
public abstract class HandleBlock {


    public static int GetFreeBlock() {
        //TODO: 根据FAT从磁盘块中找到空闲的磁盘块位置

        return 114; //FIXME
    }

    public static void SetBlockUsed(int blockNum) {
        //FIXME
    }


    public static void SetBlockFree(int blockNum) {
        //FIXME
    }

    /**
     * 挂载FAT
     * <p>FATByte 封装到 FATblock对象(单个磁盘块)</p>
     * <p>FATBlock对象 封装到 BLOCKS(磁盘块阵列)</p>
     *
     * @param BLOCKS   磁盘块阵列
     * @param FAT_Byte FAT字节对象
     * @param type     FAT类型
     */
    public static void mountFAT(List<block> BLOCKS, Byte[] FAT_Byte, Integer type) {
        switch (type) {
            case 1 -> { //FAT 1
                BLOCKS.set(0, new block(FAT_Byte));
            }
            case 2 -> { //FAT 2
                BLOCKS.set(1, new block(FAT_Byte));
            }
            default -> {
                log.warn("FAT类型错误!");
            }
        }
    }

    /**
     * 获取默认FAT1
     *
     * @return 默认FAT1
     */
    public static List<Integer> getDefaultFAT1() {

        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为指向空值
            FAT.add(Null_Pointer);
        }

        FAT.set(0, 1); //0号块指向1号块FAT1
        FAT.set(1, 2); //1号块指向2号块FAT2
        FAT.set(2, 3); //2号块指向3号块空闲块
        return FAT;
    }

    /**
     * 获取默认FAT2
     *
     * @return 默认FAT2
     */
    public static List<Integer> getDefaultFAT2() {

        List<Integer> FAT = new ArrayList<>(FAT_SIZE);
        for (int i = 0; i < FAT_SIZE; i++) { //初始化赋值全部为0
            FAT.add(Null_Pointer);
        }

        return FAT;
    }

    /**
     * 获取FAT字节对象
     * <p>List -> Bytes</p>
     *
     * @param fat FAT对象
     * @return FAT字节对象
     */
    public static Byte[] getFATBytes(List<Integer> fat) {

        Byte[] FATByte = new Byte[FAT_SIZE];
        for (int i = 0; i < FAT_SIZE; i++) {
            if (Objects.equals(fat.get(i), Null_Pointer)) { //这一项是空的
                FATByte[i] = 0;
                continue;
            }
            FATByte[i] = fat.get(i).byteValue(); //将FAT列表中每一项的值转换为字节
        }

        return FATByte;
    }


    /**
     * 获取默认格式化的BLOCKS
     *
     * @return 默认BLOCKS
     */
    public static List<block> getDefaultBLOCKS() {

        List<block> BLOCKS = new ArrayList<>(DISK_SIZE);
        for (int i = 0; i < DISK_SIZE; i++) {  //初始化赋值全部为block
            BLOCKS.add(new block());
        }

        return BLOCKS;
    }

}
