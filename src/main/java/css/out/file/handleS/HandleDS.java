package css.out.file.handleS;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.entiset.GF.DISK_SIZE;
import static css.out.file.entity.disk.initialDisk;
import static css.out.file.handleB.HandleBlock.setStr21Block;
import static css.out.file.handleB.HandleDISK.*;
import static css.out.file.handleB.HandleTXT.readAllTXT2Str;
import static css.out.file.handleB.HandleTXT.writeAllDISK2TXT;
import static css.out.file.utils.ByteUtil.str2Byte;

/**
 * 0级 磁盘系统管理工具类
 */
@Slf4j
public abstract class HandleDS {

    //! 1. 磁盘系统/

    /**
     * ?正常模式
     * <p>从磁盘映射文件完全加载磁盘系统</p>
     */
    public static void normalRebootDisk() {
        putStr2Disk(readAllTXT2Str(WORKSHOP_PATH + DISK_FILE));
        log.debug("磁盘模块重读完成");
    }


    /**
     * ?覆盖模式
     * <p>直接用当前JAVA磁盘对象覆盖磁盘映射文件</p>
     */
    public static void coverRebootDisk() {
        //手动把当前的FAT覆盖磁盘
        mountFAT(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
        //写入磁盘
        writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        putStr2Disk(readAllTXT2Str(WORKSHOP_PATH + DISK_FILE));
        log.debug("磁盘模块覆盖完成");
    }


    /**
     * ?格式化模式
     * <p>重新格式化磁盘, 会清空磁盘中的所有数据</p>
     */
    public static void cleanRebootDisk() {
        //获取新磁盘对象
        diskSyS.disk = initialDisk();
        mountFAT(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
        //写入磁盘
        writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        putStr2Disk(readAllTXT2Str(WORKSHOP_PATH + DISK_FILE));
        log.debug("{}格式化完成!", diskSyS.disk.name);
    }


    //! 2. 磁盘/


    /**
     * 获取默认格式化的空BLOCKS
     *
     * @return 默认BLOCKS
     */
    public static List<block> getVoidBLOCKS() {

        List<block> BLOCKS = new ArrayList<>(DISK_SIZE);
        for (int i = 0; i < DISK_SIZE; i++) {
            BLOCKS.add(new block());
        }

        return BLOCKS;
    }


    /**
     * Str磁盘内容赋值磁盘Java对象
     *
     * @param great_str 磁盘映射文件长字符串
     */
    public static void putStr2Disk(String great_str) {

        String[] str = great_str.split("\n");

        for (int i = 0; i < DISK_SIZE; i++) {

            if (i == FAT1_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                diskSyS.disk.FAT1 = Bytes2FAT(bytes_temp);
                mountFAT(diskSyS.disk.BLOCKS, bytes_temp, 1);
                continue;
            }

            if (i == FAT2_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                diskSyS.disk.FAT2 = Bytes2FAT(bytes_temp);
                mountFAT(diskSyS.disk.BLOCKS, bytes_temp, 2);
                continue;
            }

            setStr21Block(str[i], i); //或者是setBytes21Block
        }

//        writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE); //二次写入磁盘保证一致性
        log.debug("{}初始化完成!", diskSyS.disk.name);
    }


}
