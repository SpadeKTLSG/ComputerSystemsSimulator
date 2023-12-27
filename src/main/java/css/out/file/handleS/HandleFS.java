package css.out.file.handleS;

import css.out.file.FileApp;
import css.out.file.entity.TREE;
import css.out.file.entity.node;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static css.out.file.entiset.GF.FILE_TREE_NAME;
import static css.out.file.entiset.GF.ROOT_AUTH;
import static css.out.file.entiset.SFA.initialFileSys;
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

        //TODO
        //使用order序列遍历查找对应文件/文件夹的内容, 逐个添加到树上, 同时加到PM上

        setDefaultPM();
        setDefaultEM();


        log.debug("文件模块重读完成");
    }

    /**
     * ?覆盖模式
     * <p>直接用当前JAVA文件对象结构重建系统</p>
     * <p>根据当前Java对象的挂载关系操作</p>
     */
    public static void coverRebootFile() {
        //由于超纲, 未实现

        log.debug("文件模块覆盖完成");
    }

    /**
     * ?格式化模式
     * <p>重新格式化文件系统, 清空所有内容</p>
     *
     * @comment 你最好不要玩火...
     */
    public static void cleanRebootFile() {

        FileApp.fileSyS = initialFileSys();
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


    //! 4.CRUD FS

    /**
     * 将对象加入文件模块
     *
     * @param A 文件/文件夹对象
     */
    public static void addContentFS(Object A) {
        //?自动绑定PM和EM, Path名字和扩展名都会在创建文件FCB字节对象时候自动绑定
        //?那么文件系统剩下的工作就是加入TR了
        //? TR新增节点



    }

    /**
     * 将对象赶出文件模块
     *
     * @param A 文件/文件夹对象
     */
    public static void deleteContentFS(Object A) {


        //?需要解除PM的绑定


        //?TR删除节点

    }

    /**
     * 修改文件模块的一个对象
     *
     * @param A 被修改的对象
     * @param B 修改后的对象
     */
    public static void alterContentFS(Object A, Object B) {
        //?需要修改PM的绑定内容

        //?TR修改节点信息


    }

    /**
     * 查找文件模块的一个对象信息,这里直接返回路径
     *
     * @param A 文件/文件夹对象
     * @return 文件模块的TR生成的pathTR路径
     */
    public static String selectContentFS(Object A) {

        //?直接查找TR即可, 返回路径

        return null;
    }

}
