package css.out.file.system;

import java.util.HashMap;
import java.util.Map;

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
     * 路径管理器: 唯一
     */
    private static final Map<Integer, String> PATH_MANAGER = new HashMap<>();

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

    /**
     * 获取路径管理器
     */
    public static Map<Integer, String> initialPathManager() {
        //初始化路径管理器:初始化为0-999的Integer与空String键值对
        //初始化树形结构:根节点为/
        for (int i = 0; i < 1000; i++) {
            PATH_MANAGER.put(i, "");
        }
        PATH_MANAGER.put(0, "/"); //初始化根节点到路径映射中
        return PATH_MANAGER;
    }

    /**
     * 获取扩展名管理器
     */
    public static Map<Integer, String> initialExtendManager() {
        //初始化扩展名管理器:初始化为0-100的Integer与空String键值对
        Map<Integer, String> extendManager = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            extendManager.put(i, "");
        }
        return extendManager;
    }
}
