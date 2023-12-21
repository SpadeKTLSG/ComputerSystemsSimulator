package css.out.file;

import css.out.file.system.DiskSyS;
import css.out.file.system.FileSyS;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.handle.HandleDISK.normalRebootDisk;
import static css.out.file.handle.HandlePath.normalRebootFile;
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
        log.debug("磁盘系统开机中...");
        diskSyS = initialDiskSyS();
        log.debug("磁盘系统成员初始化完成");
        normalRebootDisk();
        log.debug("磁盘内容重读完成");

        log.debug("文件系统开机中...");
        fileSyS = initialFileSys();
        log.debug("文件系统成员初始化完成");
        normalRebootFile();
        log.debug("文件系统成员重读完成");

    }

    public void initialize() {
        //TODO
    }

    public void clean() {
        //TODO
    }

    public void start() {
        //TODO
    }

    public void reboot() {
        //TODO
    }

    public void reload() {
        log.debug("刷新中...正在重新从磁盘加载系统内容");
        diskSyS.reloadDiskSyS();
        //文档重构
        //...TODO
    }

    /**
     * 系统状态
     * //TODO 简化
     */
    public void state() {
        log.debug("文件系统状态展示");
        //打印两个系统的主要成员信息
        //1. 磁盘系统
        System.out.println(diskSyS);
        //2. 文件系统
        System.out.println(fileSyS);
    }
}
