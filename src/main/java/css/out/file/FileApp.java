package css.out.file;

import css.out.file.entiset.DiskSyS;
import css.out.file.entiset.FileSyS;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.entiset.GF.ROOT_AUTH;
import static css.out.file.entiset.SF.initialDiskSyS;
import static css.out.file.entiset.SF.initialFileSys;
import static css.out.file.handleB.HandlePATH.normalRebootFile;
import static css.out.file.handleS.HandleDS.*;

/**
 * 文件系统Application
 *
 * @author SpadeK
 */
@Slf4j
public class FileApp {

    public static DiskSyS diskSyS;

    public static FileSyS fileSyS;

    /**
     * 初始化系统成员 + 读取磁盘内容
     */
    public FileApp() {
        log.debug("磁盘模块开机中...");
        diskSyS = initialDiskSyS();
        log.debug("磁盘模块成员初始化完成");
        normalRebootDisk();
        log.debug("文件模块开机中...");
        fileSyS = initialFileSys();
        log.debug("文件模块成员初始化完成");
        normalRebootFile();
        log.debug("文件模块重读完成");
        log.info("文件系统开机完成 -by SpadeK-");
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
                log.info("///格式化磁盘 + 重建索引///");
                kickDiskRoboot();
            } else if (type.equals(2)) {
                log.info("///覆盖磁盘 + 重建索引///");
                coverDiskRoboot();
            } else if (type.equals(3)) {
                log.info("///摧毁系统, 世界毁灭吧///");
                fileSyS = null;
                diskSyS = null;

            } else {
                log.info("宁的操作不在系统操作范围内");
            }
        } else {
            log.info("宁的权限不足. 对不起, 做不到");
        }
    }

    /**
     * 重启文件系统
     */
    public void reboot() {
        log.info("文件系统重启中");
        new FileApp();
    }

    /**
     * 刷新
     * <p>重新从磁盘加载资源</p>
     */
    public void reload() {
        log.info("刷新中...正在重新从磁盘加载系统内容");
        normalRebootDisk();
        //文档重构
        //...TODO
    }

    /**
     * 展示系统状态
     */
    public void state() {
        log.info("文件系统状态展示");
        //打印两个系统的主要成员信息
        //1. 磁盘系统
        System.out.println(diskSyS);
        //2. 文件系统
        System.out.println(fileSyS);
    }

    /**
     * 覆盖模式
     */
    public void coverDiskRoboot() {
        coverRebootDisk();
        normalRebootFile(); //FIXME
    }

    /**
     * !格式化模式
     */
    public void kickDiskRoboot() {
        cleanRebootDisk();
        fileSyS = initialFileSys();//FIXME
        normalRebootFile();
    }


}
