package css.out.file.handleB;

import css.out.file.entity.FCB;
import css.out.file.entity.dir;
import css.out.file.entity.node;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.entiset.IF.AddedEXTEND;
import static css.out.file.handleB.HandleFile.str2Path;
import static css.out.file.handleS.HandleFS.initialEM;
import static css.out.file.handleS.HandleFS.initialPM;


/**
 * I级 路径序列工具类 - CRUD
 */
@Slf4j
public abstract class HandlePATH {

    //! 1. 文件系统树形结构TR


    /**
     * 初始化TR
     * <p>挂载根目录到TR</p>
     *
     * @param root 根节点
     */
    public static void setDefaultTR(node root) {

        boolean isFirst = true;

        for (ROOT_PATH root_path : ROOT_PATH.values()) {

            if (isFirst) {
                isFirst = false;
                dir tempfile = new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK);
                root.left = new node(tempfile.fcb); //挂载到根节点的左子树上

            } else {
                dir tempfile = new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK);
                node tempnode = root.left;
                while (tempnode.right != null) {
                    tempnode = tempnode.right; //递归查找根节点的左子树的最后一个右孩子节点
                }
                tempnode.right = new node(tempfile.fcb);
            }
        }
    }


    /**
     * 层次遍历TR
     *
     * @param root 文件系统树形结构根节点
     */
    public static String printTR(node root) {
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


    //针对某一节点生成TR序列
    public static String pathTR(node target) {
        return null;
    }


    //新增TR节点
    public static void addTR(FCB fcb) {
        //? 拿到FCB后, 通过String切分判断其位置, 然后组装为node, 挂载到孩子兄弟树TR上对应的位置
        //1. 拿到FCB按照"目录 : 名称"切分为两块: 目录和名称

        String[] pathTemp = fcb.pathName.split(":");
        //1.1切分目录部分, 将其按照"/"切分为数组, 每一项都是对应的目录结构(利用删除时候的鉴权保证一定存在)
        String[] dir = pathTemp[0].split("/");


    }


    //删除TR节点
    public static void deleteTR(FCB fcb) {

    }


    //查询TR节点
    public static String selectTR(FCB fcb) {
        return null;
    }


    //修改TR节点


    //! 2. 路径管理器PM


    /**
     * 初始化PM
     * <p>挂载根目录</p>
     */
    public static void setDefaultPM() {
        fileSyS.pathManager = initialPM();
        for (ROOT_PATH root_path : ROOT_PATH.values())
            bindPM(str2Path(String.valueOf(root_path)));
    }


    /**
     * PM中找pathName的键
     *
     * @param pathName 扩展名
     * @return 路径管理器中的键
     */
    public static Integer findKeyiPM(String pathName) {
        try {
            List<Integer> keys = fileSyS.pathManager.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(pathName))
                    .map(Map.Entry::getKey)
                    .toList();

            return keys.get(0);

        } catch (Exception e) {
            log.error("路径管理器中找不到对应的路径{}?!", pathName);
            throw new RuntimeException(e);
        }
    }


    /**
     * PM新增PathName
     *
     * @param pathName FCB的PathName
     * @comment 这样硬盘只需要存储对应的键即可
     */
    public static Integer bindPM(String pathName) {

        List<Integer> keys = fileSyS.pathManager.entrySet().stream() //将Map转换为Stream，过滤出值等于目标值的键值对，映射为键，收集为集合
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        //log.debug("当前路径映射器中的空白位置: {}", keys.size());
        fileSyS.pathManager.put(keys.get(0), pathName);
        return keys.get(0);
    }

    /**
     * PM删除PathName
     *
     * @param pathName FCB的PathName
     * @return 路径管理器中的键
     */
    public static Integer deletePM(String pathName) {
        List<Integer> keys = fileSyS.pathManager.entrySet().stream() //将Map转换为Stream，过滤出值等于目标值的键值对，映射为键，收集为集合
                .filter(entry -> entry.getValue().equals(pathName))
                .map(Map.Entry::getKey)
                .toList();

        fileSyS.pathManager.put(keys.get(0), "");
        return keys.get(0);
    }

    /**
     * PM定位PathName
     *
     * @param key 路径管理器中的键
     * @return FCB的PathName
     */
    public static String selectPM(Integer key) {
        return fileSyS.pathManager.get(key);
    }


    //! 3. 扩展名管理器EM


    /**
     * 初始化EM
     * <p>按照文档和文件的扩展名进行设置</p>
     */
    public static void setDefaultEM() {
        fileSyS.extendManager = initialEM();
        try {//系统自带的直接指定提高效率
            int i = 0;
            for (; i < DIR_EXTEND.size(); i++) {
                fileSyS.extendManager.put(i, DIR_EXTEND.get(i));
            }
            for (int k = 0; k < FILE_EXTEND.size(); k++) {
                fileSyS.extendManager.put(i + k, FILE_EXTEND.get(k));
            }
            for (String s : AddedEXTEND) {
                bindEM(s); //同步用户添加的扩展名
            }
        } catch (Exception e) {
            log.warn("装不下了, extendManager被撑爆咯! {}", e.getMessage());
        }

    }


    /**
     * EM中找ExtendName的键
     *
     * @param extendName 扩展名
     * @return 扩展名管理器中的键
     */
    public static Integer findKeyiEM(String extendName) {
        try {
            List<Integer> keys = fileSyS.extendManager.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(extendName))
                    .map(Map.Entry::getKey)
                    .toList();

            return keys.get(0);

        } catch (Exception e) {
            log.error("扩展名管理器中找不到对应的扩展名{}?!", extendName);
            throw new RuntimeException(e);
        }
    }


    /**
     * EM新增ExtendName
     *
     * @param extendName 扩展名
     */
    public static Integer bindEM(String extendName) {

        List<Integer> keys = fileSyS.extendManager.entrySet().stream()
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        // log.debug("当前extend中的空白位置: {}", keys.size());
        fileSyS.extendManager.put(keys.get(0), extendName);
        return keys.get(0);
    }


    /**
     * EM定位ExtendName
     *
     * @param key 键
     * @return 扩展名
     */
    public static String selectEM(Integer key) {
        return fileSyS.extendManager.get(key);
    }

}
