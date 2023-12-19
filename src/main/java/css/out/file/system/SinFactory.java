package css.out.file.system;

/**
 * 单例模式工厂
 */
public class SinFactory {

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
    private SinFactory() {
    }

    /**
     * 获取磁盘系统
     */
    public static DiskSyS initialDiskSyS() {
        return DISK_SYS;
    }

    /**
     * 获取文件系统
     */
    public static FileSyS initialFileSys() {
        return FILE_SYS;
    }
}
