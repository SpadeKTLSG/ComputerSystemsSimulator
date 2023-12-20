package css.out.file;

import css.out.file.system.DiskSyS;
import css.out.file.system.FileSyS;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.system.SinFactory.*;

/**
 * 文件系统Application
 * @author SpadeK
 */
@Slf4j
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
     * 初始化系统成员 + 读取磁盘内容
     */
    public FileApp() {
        diskSyS = initialDiskSyS();

        log.debug("磁盘系统成员初始化完成");
        fileSyS = initialFileSys();
        fileSyS.tree = initialTree();
        fileSyS.pathManager = initialPathManager();
        fileSyS.extendManager = initialExtendManager();
        log.debug("文件系统成员初始化完成");
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

    void reboot() {
        //TODO
    }


}
