package css.out.file;

import css.out.file.entiset.DiskSyS;
import css.out.file.entiset.FileSyS;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.entiset.GF.ROOT_AUTH;
import static css.out.file.handleB.HandleDISK.coverRebootDisk;
import static css.out.file.handleB.HandleDISK.normalRebootDisk;
import static css.out.file.handleB.HandlePath.normalRebootFile;
import static css.out.file.entiset.SF.initialDiskSyS;
import static css.out.file.entiset.SF.initialFileSys;

/**
 * 文件系统Application
 *
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
//        log.debug("磁盘模块开机中...");
        diskSyS = initialDiskSyS();
//        log.debug("磁盘模块成员初始化完成");
        normalRebootDisk();
        log.debug("磁盘模块重读完成");

//        log.debug("文件模块开机中...");
        fileSyS = initialFileSys();
//        log.debug("文件模块成员初始化完成");
        normalRebootFile();
        log.debug("文件模块重读完成");

    }

    /**
     * 重启磁盘系统
     *
     * @param auth 权限
     * @param type 操作类型
     */
    public FileApp(String auth, Integer type) {
        if (auth.equals(ROOT_AUTH)) { //root权限执行
            log.debug("警告, 正在使用root权限执行系统操作");
            if (type.equals(1)) {
                log.debug("格式化磁盘");
                kickDiskRoboot();
            } else if (type.equals(2)) {
                log.debug("正在执行重启文件系统操作");

            } else if (type.equals(3)) {
                log.debug("正在执行重启文件系统操作");

            } else {
                log.debug("宁的操作不在系统操作范围内");
            }
        } else {
            log.debug("宁的权限不足. 对不起, 做不到");
        }
    }

    /**
     * 重启文件系统
     */
    public void reboot() {
        log.debug("文件系统重启中");
        new FileApp();
    }

    /**
     * 刷新
     * <p>重新从磁盘加载资源</p>
     */
    public void reload() {
        log.debug("刷新中...正在重新从磁盘加载系统内容");
        normalRebootDisk();
        //文档重构
        //...TODO
    }

    /**
     * 展示系统状态
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

    /**
     * 重启并格式化磁盘
     */
    public void kickDiskRoboot() {
        diskSyS = initialDiskSyS();
        coverRebootDisk();
        log.debug("磁盘模块格式化完成");
        fileSyS = initialFileSys();
        normalRebootFile();
        log.debug("文件模块重读完成");
    }
}
