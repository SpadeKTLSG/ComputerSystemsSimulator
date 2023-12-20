package css.out.file.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static css.out.file.utils.GlobalField.DIR_EXTEND;
import static css.out.file.utils.GlobalField.FILE_EXTEND;

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
     * 扩展名管理器: 唯一
     */
    private static final Map<Integer, String> extendManager = new HashMap<>();

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
     * <p>初始化扩展名管理器:初始化为0-100的Integer与空String键值对</p>
     */
    public static Map<Integer, String> initialExtendManager() {

        for (int i = 0; i < 100; i++) {
            extendManager.put(i, "");
        }
        setExtendManager();

        return extendManager;
    }

    /**
     * 设置扩展名管理器
     * <p>按照文档和文件的扩展名进行设置</p>
     */
    public static void setExtendManager() {

        List<String> temp = Collections.singletonList(DIR_EXTEND); //将DIR_EXTEND与FILE_EXTEND_DEFAULT列表读取到一个List列表后依次放入扩展名管理器
        int i = 0;
        for (; i < temp.size(); i++) {
            extendManager.put(i, temp.get(i));
        }
        temp = FILE_EXTEND;
        for (; i < temp.size(); i++) {
            extendManager.put(i, temp.get(i));
        }

    }
}
