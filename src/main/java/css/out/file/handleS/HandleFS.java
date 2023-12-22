package css.out.file.handleS;

import css.out.file.entity.TREE;
import css.out.file.entity.node;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.entiset.GF.FILE_TREE_NAME;
import static css.out.file.entiset.GF.ROOT_AUTH;
import static css.out.file.entiset.IF.AddedEXTEND;
import static css.out.file.handleB.HandlePath.*;

/**
 * 0级 文件系统管理工具类
 */
@Slf4j
public abstract class HandleFS {

    //! 0. 文件系统

    /**
     * 正常从磁盘完全加载文件系统
     */
    public static void normalRebootFile() {
        //需要从磁盘读取当前文件树信息, 在基础索引树的基础上重建
        //因为设置了一个块里只能有一个文件(整个/部分), 因此需要按照FAT的顺序遍历磁盘, 读取每个文件对象(file/dir)的字节流, 转换为对象, 挂载到树上

        //TODO 最后来

        //挂载根目录到文件路径管理器PM
        for (ROOT_PATH root_path : ROOT_PATH.values()) {
            bindPM(getROOT_DIRPath(root_path));
        }
        setDefaultEM();
        //加载扩展名管理器EM, 同步用户添加的扩展名
        for (String s : AddedEXTEND) { //在扩展名管理器中找一个空位填入
            bindEM(s);
        }


        log.debug("文件模块重读完成");
    }

    //! 1. 文件系统树形结构TR/

    /**
     * 获取初始TR
     */
    public static TREE initialTree() {
        TREE tree = new TREE();
        tree.name = FILE_TREE_NAME;
        tree.root = new node(ROOT_AUTH); //挂载根节点
        mountROOT_DIR2Tr(tree.root); //挂载根目录下的8个文件夹
        return tree;
    }


    //! 2. 路径管理器PM/

    /**
     * 获取初始PM
     */
    public static void initialPM() {
        for (int i = 0; i < 1000; i++) fileSyS.pathManager.put(i, "");
        fileSyS.pathManager.put(0, "/"); //根节点
    }


    /**
     * 路径管理器中找FCB的PathName
     *
     * @param key 路径管理器中的键
     * @return FCB的PathName
     */
    public static String fromPM(Integer key) {
        return fileSyS.pathManager.get(key);
    }


    //! 3. 扩展名管理器EM/

    /**
     * 获取扩展名管理器
     * <p>初始化为0-100的Integer与空String键值对</p>
     */
    public static void initialEM() {
        for (int i = 0; i < 100; i++) fileSyS.extendManager.put(i, "");
    }


}
