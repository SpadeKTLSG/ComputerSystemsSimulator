package css.out.file.handle;

import css.out.file.entity.FCB;
import css.out.file.entity.TREE;
import css.out.file.entity.dir;
import css.out.file.entity.node;
import css.out.file.enums.FileDirTYPE;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.entiset.IF.AddedEXTEND;
import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;


/**
 * I级 路径序列工具类
 */
@Slf4j
public abstract class HandlePath {

    /**
     * 获得根路径下的默认路径
     *
     * @param path 八大路径之一
     * @return 对应路径的String序列
     */
    public static String getROOT_DIRPath(ROOT_PATH path) {

        return '/' + String.valueOf(path);
    }


    /**
     * FCB的PathName绑定路径管理器
     * <p>这样硬盘只需要存储对应的键即可</p>
     *
     * @param fcb FCB
     * @return 绑定的键
     */
    public static Integer bindPathManager(FCB fcb) {

        //在PathManager找一个空白位置插入索引项
        List<Integer> keys = fileSyS.pathManager.entrySet().stream() //将Map转换为Stream，过滤出值等于目标值的键值对，映射为键，收集为集合
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        log.debug("当前路径映射器中的空白位置: {}", keys.size());
        Integer A = keys.get(0);
        fileSyS.pathManager.put(A, fcb.getPathName());

        return A;
    }

    /**
     * 路径管理器中找FCB的PathName
     *
     * @param key 路径管理器中的键
     * @return FCB的PathName
     */
    public static String fromPathManager(Integer key) {
        return fileSyS.pathManager.get(key);
    }

    /**
     * FCB的ExtendName绑定扩展名管理器
     * <p>新增操作, 一般没有实现</p>
     * <p>这样硬盘只需要存储对应的键即可</p>
     *
     * @param fcb FCB
     * @return 绑定的键
     */
    public static Integer bindExtendManager(FCB fcb) {


        List<Integer> keys = fileSyS.extendManager.entrySet().stream()       //在extendManager找一个空白位置插入索引项
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

//        log.debug("当前extend中的空白位置: {}", keys.size());
        Integer A = keys.get(0);
        fileSyS.extendManager.put(A, fcb.getExtendName());

        return A;
    }

    /**
     * 扩展名管理器中找FCB的ExtendName
     *
     * @param fcb FCB
     * @return 扩展名管理器中的键
     */
    public static Integer selectExtendManager(FCB fcb) {
        //在extendManager找对应的键, 找不到报错
        try {
            List<Integer> keys = fileSyS.extendManager.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(fcb.getExtendName()))
                    .map(Map.Entry::getKey)
                    .toList();

            return keys.get(0);
        } catch (Exception e) {
            log.error("扩展名管理器中找不到对应的扩展名{}?!", fcb.getExtendName());
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据键从扩展名管理器中扩展名(值)
     *
     * @param key 键
     * @return 扩展名
     */
    public static String fromExtendManager(Integer key) {
        return fileSyS.extendManager.get(key);
    }


    /**
     * FCB的TypeFlag标识转Int
     *
     * @param fcb FCB
     * @return 0: 文件, 1: 文件夹
     */
    public static Integer FileorDir2Int(FCB fcb) {

        return fcb.getTypeFlag().equals(FILE) ? 0 : 1;
    }

    /**
     * Int转FCB的TypeFlag标识
     *
     * @param num 0: 文件, 1: 文件夹
     * @return FCB的TypeFlag标识
     */
    public static FileDirTYPE Int2FileorDir(Integer num) {

        return num.equals(0) ? FILE : DIR;
    }

    /**
     * 文件系统树形结构的层次遍历
     *
     * @param root 文件系统树形结构根节点
     */
    public static String printTree(node root) {
        LinkedList<node> queue = new LinkedList<>();
        //使用Stringbuffer存储输出结果
        StringBuilder sb = new StringBuilder();

        queue.offer(root);
        while (!queue.isEmpty()) {
            node node = queue.poll();
            sb.append(node.fcb.pathName).append(" ");

            if (node.right != null) queue.offer(node.right); // 如果结点有兄弟结点，将其入队
            if (node.left != null) queue.offer(node.left); // 如果结点有孩子结点，将其入队

        }
        return sb.toString();
    }

    /**
     * 挂载根目录到树上, 需要对应根目录的FCB
     */
    public static void mountROOT_DIR2Tree(node root) {
        //遍历枚举类, 挂载根目录下的8个文件夹到根目录上, 通过树的操作实现
        boolean isFirst = true;
        for (ROOT_PATH root_path : ROOT_PATH.values()) {
            if (isFirst) {
                isFirst = false;
                dir tempfile = new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK);
                root.left = new node(tempfile.fcb); //挂载到根节点的左子树上
            } else {
                //获得一个根目录下的文件夹, 将其作为root的孩子节点的兄弟节点;count+1;
                dir tempfile = new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK);
                //递归查找根节点的左子树的最后一个右孩子节点, 将其右孩子节点设置为tempfile
                node tempnode = root.left;
                while (tempnode.right != null) {
                    tempnode = tempnode.right;
                }
                tempnode.right = new node(tempfile.fcb);
            }
        }
    }

    /**
     * 获取文件系统树形结构
     */
    public static TREE initialTree() {
        TREE tree = new TREE();
        tree.name = FILE_TREE_NAME;
        tree.root = new node(ROOT_AUTH); //挂载根节点
        mountROOT_DIR2Tree(tree.root); //挂载根目录下的8个文件夹
        return tree;
    }

    /**
     * 获取路径管理器
     */
    public static Map<Integer, String> initialPathManager() {
        Map<Integer, String> pathManager = new HashMap<>();
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
        Map<Integer, String> extendManager = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            extendManager.put(i, "");
        }

        setExtendManager(extendManager);
        return extendManager;
    }

    /**
     * 设置扩展名管理器
     * <p>按照文档和文件的扩展名进行设置</p>
     */
    public static void setExtendManager(Map<Integer, String> extendManager) {

        try {
            int i = 0;
            for (; i < DIR_EXTEND.size(); i++) {
                extendManager.put(i, DIR_EXTEND.get(i));
            }
            for (int k = 0; k < FILE_EXTEND.size(); k++) {
                extendManager.put(i + k, FILE_EXTEND.get(k));
            }
        } catch (Exception e) {
            log.warn("装不下了, extendManager被撑爆咯! {}", e.getMessage());
        }

    }


    /**
     * 正常从磁盘完全加载文件系统
     */
    public static void normalRebootFile() {
        //需要从磁盘读取当前文件树信息, 在基础索引树的基础上重建
        //因为设置了一个块里只能有一个文件(整个/部分), 因此需要按照FAT的顺序遍历磁盘, 读取每个文件对象(file/dir)的字节流, 转换为对象, 挂载到树上

        //TODO 最后来

        //加载扩展名管理器, 同步用户添加的扩展名
        for (String s : AddedEXTEND) { //在扩展名管理器中找一个空位填入

            List<Integer> keys = fileSyS.extendManager.entrySet().stream()
                    .filter(entry -> entry.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .toList();
            fileSyS.extendManager.put(keys.get(0), s);
        }

        //挂载根目录到文件路径管理器上, 需要对应根目录的FCB
        //利用根目录的枚举遍历
        for (ROOT_PATH root_path : ROOT_PATH.values()) {
            //文件路径, 不是树!
            bindPathManager(new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK).fcb);
        }


    }
}
