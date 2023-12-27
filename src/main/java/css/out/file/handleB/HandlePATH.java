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
import static css.out.file.handleB.HandleFile.*;
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


    //新增TR节点
    public static void addTR(FCB fcb) { //fcb或者是node皆可
        //? 拿到FCB后, 通过String切分判断其上级已存在node的位置
        //1. 拿到FCB按照"目录 : 名称"切分为两块: 目录和名称

        String[] pathTemp = fcb.pathName.split(":");
        //1.1

        String[] dir = getPathArray(fcb);
        String name = getName(fcb);
        node temp_node = fileSyS.tree.root.left;            //创建游标指针tempnode去TR中跟踪序列, 指向根节点的左子树, 也就是第一个挂载到/的节点
        StringBuilder sb = new StringBuilder();     //创建SB存储目录序列


        //2. 遍历目录数组, 每次遍历到一个目录, 就判断其是否是tempnode的左子树, 如果是, 就将tempnode指向其左子树, 如果不是, 就将tempnode指向其右子树
        node dir_temp = searchNode(temp_node, dir, 0, sb); //得到要新增的节点的父节点
        System.out.println(sb); //展示目录序列


        //?新增逻辑: fcb/node组装为node, 挂载到孩子兄弟树TR上父节点的左孩子或者是左孩子的右兄弟上

    }

    /**
     * 递归搜索TR节点
     *
     * @param root  根节点
     * @param dir   目录数组
     * @param index 目录数组的索引
     * @param sb    目录序列
     * @return 返回定位到的节点
     */
    static node searchNode(node root, String[] dir, int index, StringBuilder sb) {
        //从根节点的左子树开始遍历, 每次遍历到一个节点, 就判断其是否是目录数组中的目录, 如果是, 就将tempnode指向其左子树, 如果不是, 就将tempnode指向其右子树
        //如果遍历到最后一个目录, 就将tempnode指向其左子树, 然后将其右子树指向新节点
        if (root == null) {
            return null;
        }

        if (root.fcb.pathName.equals(dir[index])) { //比对当前节点的fcb的pathName切分后提取出的文件名和目录数组中的目录是否相同
            // 如果这是目录数组的最后一个元素, 定位到了, 则返回当前节点
            if (index == dir.length - 1) {
                sb.append(root.fcb.pathName.split(":")[1]).append("/"); //将当前root指向的fcb的文件名拼接到sb中
                return root;
            }

            // 否则，还得继续在左子树中搜索下一个目录
            else {
                sb.append(root.fcb.pathName.split(":")[1]).append("/"); //将当前root指向的fcb的文件名拼接到sb中
                return searchNode(root.left, dir, index + 1, sb);
            }
        }

        // 在右子树中搜索
        return searchNode(root.right, dir, index, sb);
    }

    //删除TR节点
    public static void deleteTR(FCB fcb) {
        //? 拿到FCB后, 通过String切分判断其位置, 定位到地点后执行孩子兄弟树的删除节点操作

    }


    //查询TR节点
    public static String selectTR(FCB fcb) {
        //? 拿到FCB后, 通过String切分判断其位置(树上意义无意义),因此暂时直接调用pathTR返回类似order的路径
//        pathTR(new node(fcb));
        return null;
    }


    //针对某一节点生成TR序列
    public static String pathTR(node target) {
        //? 使用StringBuilder拼接从根目录root节点到target节点的路径


        return null;
    }


    //修改TR节点
    public static void alterTR(FCB fcb1, FCB fcb2) {
        //? 拿到FCB1后, 通过String切分判断其位置, 定位到地点后执行孩子兄弟树的修改节点操作
        //? 直接替换内容也可

    }


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
