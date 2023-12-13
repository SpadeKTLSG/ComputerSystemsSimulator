package css.out.file;

import css.out.file.actor.DiskSyS;
import css.out.file.actor.FileSyS;

/**
 * 文件系统
 */
public class FileApp {

    /**
     * 磁盘系统
     */
    public DiskSyS diskSyS;

    /**
     * 文件系统
     */
    public FileSyS fileSyS;

    void FtoD() {
        //模拟磁盘系统向文件系统调用
        this.diskSyS = new DiskSyS();
        this.fileSyS = new FileSyS();

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
