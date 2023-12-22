package css.out.file.handleB;

import css.out.file.entity.dir;
import css.out.file.entity.node;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.entiset.GF.*;


/**
 * I级 路径序列工具类
 */
@Slf4j
public abstract class HandlePath {

    //! 1. 文件系统树形结构TR - CRUD


    /**
     * 层次遍历TR
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
     * 挂载根目录到TR
     *
     * @param root 根节点
     */
    public static void mountROOT_DIR2Tr(node root) {
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


    //! 2. 路径管理器PM - CRUD


    /**
     * 获得根路径下的默认路径
     *
     * @param path 八大路径之一
     * @return 对应路径的String序列
     */
    public static String getROOT_DIRPath(ROOT_PATH path) {
        //这里只是简单实现了
        return '/' + String.valueOf(path);
    }


    /**
     * FCBPathName绑定PM
     *
     * @param pathName FCB的PathName
     * @comment 这样硬盘只需要存储对应的键即可
     */
    public static void bindPM(String pathName) {

        //在PathManager找一个空白位置插入索引项
        List<Integer> keys = fileSyS.pathManager.entrySet().stream() //将Map转换为Stream，过滤出值等于目标值的键值对，映射为键，收集为集合
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        //log.debug("当前路径映射器中的空白位置: {}", keys.size());
        Integer A = keys.get(0);
        fileSyS.pathManager.put(A, pathName);

    }


    //! 3. 扩展名管理器EM - CRUD


    /**
     * 初始化扩展名管理器
     * <p>按照文档和文件的扩展名进行设置</p>
     */
    public static void setDefaultEM() {

        try {
            int i = 0;
            for (; i < DIR_EXTEND.size(); i++) {
                fileSyS.extendManager.put(i, DIR_EXTEND.get(i));
            }
            for (int k = 0; k < FILE_EXTEND.size(); k++) {
                fileSyS.extendManager.put(i + k, FILE_EXTEND.get(k));
            }

        } catch (Exception e) {
            log.warn("装不下了, extendManager被撑爆咯! {}", e.getMessage());
        }

    }


    /**
     * 扩展名管理器中找FCB的ExtendName
     *
     * @param extendName 扩展名
     * @return 扩展名管理器中的键
     */
    public static Integer selectEM(String extendName) {
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
     * FCB的ExtendName直接Str绑定扩展名管理器
     * <p>新增操作, 这里简化了没有实现</p>
     *
     * @param extendName 扩展名
     */
    public static void bindEM(String extendName) {

        List<Integer> keys = fileSyS.extendManager.entrySet().stream()
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        // log.debug("当前extend中的空白位置: {}", keys.size());
        fileSyS.extendManager.put(keys.get(0), extendName);

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

}
