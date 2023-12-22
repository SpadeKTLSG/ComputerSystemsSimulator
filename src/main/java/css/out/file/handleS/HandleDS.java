package css.out.file.handleS;

import lombok.extern.slf4j.Slf4j;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.entity.disk.initialDisk;
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
        //手动把当前的FAT覆盖磁盘
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
        //写入磁盘
        writeAllDISK(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        reloadStr2Disk(readAllDISK(WORKSHOP_PATH + DISK_FILE));
        log.debug("磁盘模块覆盖完成");
    }


    /**
     * !完全重新格式化磁盘
     * <p>重新格式化磁盘, 会清空磁盘中的所有数据</p>
     */
    public static void totalReloadDisk() {
        //获取新磁盘对象
        diskSyS.disk = initialDisk();
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT(diskSyS.disk.BLOCKS, getFATBytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
        //写入磁盘
        writeAllDISK(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        reloadStr2Disk(readAllDISK(WORKSHOP_PATH + DISK_FILE));
        log.debug("{}格式化完成!", diskSyS.disk.name);
    }


}