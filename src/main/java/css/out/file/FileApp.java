package css.out.file;

import css.out.file.system.DiskSyS;
import css.out.file.system.FileSyS;

import static css.out.file.utils.SinFactory.initialDiskSyS;
import static css.out.file.utils.SinFactory.initialFileSys;

/**
 * 文件系统Application
 */
public class FileApp {

    /**
     * 磁盘系统
     */
    public static DiskSyS diskSyS;

    /**
     * 文件系统
     */
    public static FileSyS fileSyS;

    /**
     * 初始化两个static系统
     * TODO 单例优化
     */
    public FileApp() {
        diskSyS = initialDiskSyS();
        fileSyS = initialFileSys();
    }

    void initialize() {
        //TODO
    }

    void clean() {
        //TODO
    }

    void start() {
        //TODO
    }


}
