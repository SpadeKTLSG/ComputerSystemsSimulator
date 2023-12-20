package css.out.file.system;

import css.out.file.entity.TREE;
import css.out.file.entity.node;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static css.out.file.entity.GlobalField.*;

/**
 * 单例模式工厂
 */
@Slf4j
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
    private static final Map<Integer, String> pathManager = new HashMap<>();

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
     * 获取文件系统树形结构
     */
    public static TREE initialTree() {
        TREE tree = new TREE();
        tree.name = FILE_TREE_NAME;
        tree.root = new node(ROOT_AUTH); //挂载根节点
        tree.mountROOT_DIR(); //挂载根目录下的8个文件夹
        return tree;
    }

    /**
     * 获取路径管理器
     */
    public static Map<Integer, String> initialPathManager() {
        //初始化路径管理器:初始化为0-999的Integer与空String键值对
        //初始化树形结构:根节点为/
        for (int i = 0; i < 1000; i++) {
            pathManager.put(i, "");
        }
        pathManager.put(0, "/"); //初始化根节点到路径映射中
        return pathManager;
    }

    /**
     * 获取扩展名管理器
     * <p>初始化扩展名管理器:初始化为0-100的Integer与空String键值对</p>
     */
    public static Map<Integer, String> initialExtendManager() {


        for (int i = extendManager.size(); i < 100; i++) {
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

        try {
            int i = 0;
            for (; i < DIR_EXTEND.size(); i++) {
                extendManager.put(i, DIR_EXTEND.get(i));
            }
            for (int k = 0; k < FILE_EXTEND.size(); k++) {
                extendManager.put(i + k, FILE_EXTEND.get(k));
            }
        } catch (Exception e) {
            log.warn("extendManager被撑爆咯!");
            throw new RuntimeException(e);
        }

    }
}
