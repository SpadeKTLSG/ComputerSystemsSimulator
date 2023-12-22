package css.out.file.handleS;

import css.out.file.entity.TREE;
import css.out.file.entity.node;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static css.out.file.entiset.GF.FILE_TREE_NAME;
import static css.out.file.entiset.GF.ROOT_AUTH;
import static css.out.file.handleB.HandlePATH.*;

/**
 * 0级 文件系统管理工具类
 */
@Slf4j
public abstract class HandleFS {

    //! 0. 文件系统/

    /**
     * ?正常模式
     * <p>正常从磁盘完全刷新文件系统</p>
     */
    public static void normalRebootFile() {
        //需要从磁盘读取当前文件树信息, 在基础索引树的基础上重建
        //因为设置了一个块里只能有一个文件(整个/部分), 因此需要按照FAT的顺序遍历磁盘, 读取每个文件对象(file/dir)的字节流, 转换为对象, 挂载到树上

        //TODO 最后来

        setDefaultPM();
        setDefaultEM();


        log.debug("文件模块重读完成");
    }

    /**
     * ?覆盖模式
     * <p>直接用当前JAVA磁盘对象覆盖文件系统对象</p>
     *
     * @comment 这个行动完全没有意义可言
     */
    public static void coverRebootFile() {


        log.debug("文件模块覆盖完成");
    }

    /**
     * ?格式化模式
     * <p>重新格式化文件系统, 清空所有内容</p>
     *
     * @comment 你最好不要玩火...
     */
    public static void cleanRebootFile() {


        log.debug("文件模块格式化完成");
    }
    //! 1. 文件系统树形结构TR/


    /**
     * 获取初始TR
     */
    public static TREE initialTR() {
        TREE tree = new TREE();
        tree.name = FILE_TREE_NAME;
        tree.root = new node(ROOT_AUTH); //挂载根节点
        setDefaultTR(tree.root);
        return tree;
    }


    //! 2. 路径管理器PM/


    /**
     * 获取初始PM
     */
    public static Map<Integer, String> initialPM() {
        Map<Integer, String> pm = new HashMap<>();
        for (int i = 0; i < 1000; i++) pm.put(i, "");
        pm.put(0, "/"); //根节点
        return pm;
    }


    //! 3. 扩展名管理器EM/


    /**
     * 获取扩展名管理器
     * <p>初始化为0-100的Integer与空String键值对</p>
     */
    public static Map<Integer, String> initialEM() {
        Map<Integer, String> em = new HashMap<>();
        for (int i = 0; i < 100; i++) em.put(i, "");
        return em;
    }


}
