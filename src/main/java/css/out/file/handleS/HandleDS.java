package css.out.file.handleS;

import lombok.extern.slf4j.Slf4j;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.handleB.HandleBlock.*;
import static css.out.file.handleB.HandleDISK.*;

/**
 * 0级 磁盘系统管理工具类
 */
@Slf4j
public abstract class HandleDS {

    /**
     * !正常模式
     * <p>从磁盘映射文件完全加载磁盘系统</p>
     */
    public static void normalRebootDisk() {
        reloadStr2Disk(readAllDISK(WORKSHOP_PATH + DISK_FILE));
        //通信...
    }


    /**
     * !覆盖模式
     * <p>直接用当前JAVA磁盘对象覆盖磁盘映射文件</p>
     */
    public static void coverRebootDisk() {
        //需要手动把初始FAT覆盖磁盘, 因为默认是从磁盘读
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT2), 2); //挂载FAT1字节对象
        //然后才能写入磁盘
        writeAllDISK(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        reloadStr2Disk(readAllDISK(WORKSHOP_PATH + DISK_FILE));
        log.debug("磁盘模块覆盖完成");
    }


    /**
     * !完全重新格式化磁盘
     * <p>重新格式化磁盘, 会清空磁盘中的所有数据</p>
     */
    public static void totalReloadDisk() {
//        diskSyS.disk.name = DISK_NAME;
//        diskSyS.disk.BLOCKS = getDefaultBLOCKS(); //获得磁盘空间
//
//        diskSyS.disk.FAT1 = getDefaultFAT1(); //获得FAT1对象
//        diskSyS.disk.FAT2 = getDefaultFAT2(); //获得FAT2对象
        initialDisk(diskSyS.disk);
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象

        writeAllDISK(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE); //写入磁盘
        log.debug("{}格式化完成!", diskSyS.disk.name);
    }


}
