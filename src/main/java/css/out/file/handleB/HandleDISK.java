package css.out.file.handleB;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.api.CommonApiList.alertUser;
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


    /**
     * 指定FAT1为全满状态, 下一个空闲位置位于FAT2
     * <p>或: 指定FAT2为全满状态,全部被占满</p>
     *
     * @param type 1: FAT1 2: FAT2
     */
    public static void fullFillFAT(Integer type) {
        List<Integer> FAT1 = new ArrayList<>(FAT_SIZE);
        int i = 0;
        for (; i < FAT_SIZE; i++)
            FAT1.add(i + 1);

        FAT1.set(FAT_SIZE - 1, FAT_SIZE);

        diskSyS.disk.FAT1 = FAT1;

        if (type == 2) { //还要将FAT2设置为全满
            List<Integer> FAT2 = new ArrayList<>(FAT_SIZE);

            for (; i < FAT_SIZE * 2; i++)
                FAT2.add(i + 1);

            FAT2.set(FAT_SIZE - 1, Null_Pointer);

            diskSyS.disk.FAT2 = FAT2;
        }
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
    }


    /**
     * 利用FAT构建当前磁盘块的顺序访问序列order
     *
     * @return 顺序访问的盘块号List序列
     */
    public static List<Integer> getFATOrder() {

        int pos = 0;
        List<Integer> order = new ArrayList<>(); //顺序访问的盘块号
        boolean isFAT1 = false;

        for (int i = 0; i < FAT_SIZE; i++) { //第一个FAT表遍历
            if (!Objects.equals(diskSyS.disk.FAT1.get(pos), Null_Pointer)) {
                pos = diskSyS.disk.FAT1.get(pos);
                order.add(pos);

            } else {
                isFAT1 = true; //如果是有空闲块的退出则反转标志位
                break;
            }

        }


        if (!isFAT1) {//正常退出后去找FAT2
            pos = FAT_SIZE;

            for (int i = 0; i < FAT_SIZE - 1; i++) { //第二个FAT表遍历到最后一个元素前

                if (!Objects.equals(diskSyS.disk.FAT2.get(pos - FAT_SIZE), Null_Pointer)) {
                    pos = diskSyS.disk.FAT2.get(pos - FAT_SIZE);
                    order.add(pos);

                } else {
                    isFAT1 = true;  //如果是有空闲块的退出则反转
                    break;
                }

            }
        }


        if (!isFAT1) { //还是找不到退出?
            log.debug("FATorder序列已经满了:{}", order);
        }

        return order;
    }


    /**
     * FATorder序列中查找第一个空闲块
     *
     * @return FAT序列的综合下标; if -1: 没有找到空闲块
     */
    public static Integer get1FreeBlock() {
        List<Integer> order = getFATOrder();

        if (order.size() == FAT_SIZE * 2 - 1) {
            log.warn("FAT1和FAT2都装不下咯!, 当前FAT状态: FAT1: {}, FAT2: {}", diskSyS.disk.FAT1, diskSyS.disk.FAT2);
            alertUser("磁盘被撑爆了, Behave yourself!");
            //throw new RuntimeException("没有找到空闲块!");
            return -1;
        }

        return order.get(order.size() - 1); //返回最后一项的位置
    }


    //FAT序列中占用第一个空闲块, 返回逻辑FAT位置
    public static Integer set1BlockUse() {
        Integer target = get1FreeBlock();
        //判断其位于FAT1还是FAT2中
        boolean isFAT1 = target < FAT_SIZE;

        List<Integer> order = getFATOrder();
        if (order.size() == FAT_SIZE * 2) {
            log.warn("FAT1和FAT2都装不下咯!, 当前FAT状态: FAT1: {}, FAT2: {}", diskSyS.disk.FAT1, diskSyS.disk.FAT2);
            alertUser("磁盘被撑爆了, Behave yourself!");
            //throw new RuntimeException("没有找到空闲块!");
            return -1;
        }


        if (isFAT1) {
            //标记其上一个路径序列中的元素指向其自身
            //如果是在FAT1中找到的, 则在FAT1中设置
        }
//            diskSyS.disk.FAT1.set(pos, pos);
        else {

            //如果是在FAT2中找到的, 则在FAT2中设置
        }


        //同步后, 将其值修改为下一个空闲块位置


        //TODO 持久化BLOCKS
        //TODO 刷新到TXT
        return -1;
    }


    /**
     * 释放block空间: 将FAT最晚的一个块指向这个位置, 这个位置同样指向空(514)
     *
     * @param blockNum
     */
    public static void set1BlockFree(int blockNum) {
        //设置为空

        //将队列的最后一项指向当前这个末尾节点, 同时将当前这个末尾节点指向NULL_POINTER
        //如果是在FAT1中找到的, 则在FAT1中设置; 如果是在FAT2中找到的, 则在FAT2中设置
//        if (order.size() > 1) //如果有多个块, 则将最后一个块指向NULL_POINTER
//            if (Objects.equals(diskSyS.disk.FAT1.get(order.get(order.size() - 2)), Null_Pointer)) //如果是在FAT1中找到的, 则在FAT1中设置
//                diskSyS.disk.FAT1.set(order.get(order.size() - 2), Null_Pointer);
//            else //如果是在FAT2中找到的, 则在FAT2中设置
//                diskSyS.disk.FAT2.set(order.get(order.size() - 2), Null_Pointer);

    }

}
