package css.out.file.handleB;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.handleB.HandleTXT.write1Str2TXT;
import static css.out.file.utils.ByteUtil.byte2Str;
import static css.out.file.utils.ByteUtil.str2Byte;

/**
 * I级 磁盘(TXT)管理工具类
 */
@Slf4j
public abstract class HandleDISK {

    //! 1. 磁盘 - CRUD

    //TODO: 根据FAT从磁盘中找到1个空闲的磁盘块位置
    public static int get1FreeBlock() {

        //在全局的FAT中查找第一个值为514的位置, 同时将FAT最晚的一个块指向这个位置, 这个位置同样指向空(514)
        //返回这个位置的块号

        //对象: diskSyS.disk.FAT1 + FAT2, 从开始盘块:全局变量FAT1开始指针查找, 将对应位置的值作为下一个访问的位置, 直到读取到514代表空; 而后写入新的对象
        //如果FAT1遍历完了, 仍然没有找到空闲块, 则去FAT2查找, 仍然没有找到就报错
        int pos = FAT1_DIR; //从FAT1开始
        for (int i = 0; i < FAT_SIZE; i++) {
            if (diskSyS.disk.FAT1.get(pos) == Null_Pointer) {
                diskSyS.disk.FAT1.set(pos, Null_Pointer);
                diskSyS.disk.FAT2.set(pos, Null_Pointer);
                return pos;
            }
            pos = diskSyS.disk.FAT1.get(pos);
        }

        return 114;
    }


    /**
     * 释放block空间
     *
     * @param blockNum
     */
    public static void set1BlockFree(int blockNum) {
        //设置为空
        //FIXME
    }


    /**
     * 字节数组设置1个block内容+TXT位置写入
     *
     * @param bytes    字节数组内容
     * @param blockNum 块号
     */
    public static void setBytes21Block(byte[] bytes, int blockNum) {
        diskSyS.disk.BLOCKS.set(blockNum, new block(bytes));
        write1Str2TXT(byte2Str(bytes), WORKSHOP_PATH + DISK_FILE, blockNum);
    }


    /**
     * 字符串设置1个block内容+TXT位置写入
     *
     * @param str      字符串内容
     * @param blockNum 块号
     */
    public static void setStr21Block(String str, int blockNum) {
        diskSyS.disk.BLOCKS.set(blockNum, new block(str2Byte(str)));
        write1Str2TXT(str, WORKSHOP_PATH + DISK_FILE, blockNum);
    }



    //! 2. FAT - CRUD


    /**
     * 挂载FAT到 BLOCKS
     *
     * @param BLOCKS   磁盘块阵列
     * @param FAT_Byte FAT字节对象
     * @param type     FAT类型: 1 or 2
     */
    public static void mountFAT2BLOCKS(List<block> BLOCKS, byte[] FAT_Byte, Integer type) {
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

        for (int i = 0; i < FAT_SIZE; i++)
            FAT.add(Null_Pointer);

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

            if (Objects.equals(fat.get(i), Null_Pointer)) //如果这一项是空的, 就在磁盘上写0(空)
                FATByte[i] = 0;

            else
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

        for (int i = 0; i < FAT_SIZE; i++)
            FAT.add(Null_Pointer);

        for (int i = 0; i < FAT_SIZE; i++)
            if ((int) bytes[i] != 0)
                FAT.set(i, (int) bytes[i]);

        return FAT;
    }


}
