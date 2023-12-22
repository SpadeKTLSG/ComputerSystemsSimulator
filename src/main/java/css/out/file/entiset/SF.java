package css.out.file.entiset;

import lombok.extern.slf4j.Slf4j;

import static css.out.file.handleB.HandleDISK.initialDisk;
import static css.out.file.handleB.HandlePath.*;

/**
 * 单例模式工厂
 */
@Slf4j
public abstract class SF {

    /**
     * DiskSyS磁盘系统: 唯一
     */
    private static final DiskSyS DISK_SYS = new DiskSyS();

    /**
     * FileSyS文件系统: 唯一
     */
    private static final FileSyS FILE_SYS = new FileSyS();


    /**
     * private构造
     * 保证外部无法实例化
     */
    private SF() {
    }

    /**
     * 获取磁盘系统
     */
    public static DiskSyS initialDiskSyS() {
        initialDisk(DISK_SYS.disk);
        return DISK_SYS;
    }

    /**
     * 获取文件系统
     */
    public static FileSyS initialFileSys() {
        FILE_SYS.tree = initialTree();
        FILE_SYS.pathManager = initialPathManager();
        FILE_SYS.extendManager = initialExtendManager();
        return FILE_SYS;
    }


}