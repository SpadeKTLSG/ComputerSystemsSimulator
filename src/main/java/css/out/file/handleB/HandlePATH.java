package css.out.file.handleB;

import css.out.file.entity.FCB;
import css.out.file.entity.dir;
import css.out.file.entity.node;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.api.CommonApiList.alertUser;
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
                dir tempdir = new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK);
                root.left = new node(tempdir.fcb); //挂载到根节点的左子树上

            } else {
                dir tempdir = new dir("/:" + root_path.getName(), ROOT_DIR_BLOCK);
                node tempnode = root.left;
                while (tempnode.right != null) {
                    tempnode = tempnode.right; //递归查找根节点的左子树的最后一个右孩子节点
                }
                tempnode.right = new node(tempdir.fcb);
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


    /**
     * 新增TR节点
     *
     * @param fcb 新增对象的FCB
     */
    public static void addTR(FCB fcb) { //fcb或者是node皆可
        //? 拿到FCB后, 通过String切分判断其上级已存在node的位置, 封装到searchUpperNode方法中
        node dir_temp = searchUpperNode(fcb);

        if (dir_temp == null) {
            return;
        }

        //?新增逻辑: fcb/node组装为node, 挂载到孩子兄弟树TR上父节点的左孩子或者是左孩子的右兄弟上

        //FIXME 判定当前父节点下有没有和它重名的对象?

        node input = new node(fcb); //直接将fcb封装为node, 左右子节点均为null

        if (dir_temp.left == null) {
            dir_temp.left = input; //如果父节点的左孩子为空, 直接挂载到左孩子上
        } else {
            node tempnode = dir_temp.left;
            while (tempnode.right != null) {
                tempnode = tempnode.right; //递归查找根节点的左子树的最后一个右孩子节点
            }
            tempnode.right = input;//将新节点挂载到最后一个右孩子节点的右兄弟上
        }

    }


    /**
     * 删除TR节点
     *
     * @param fcb 删除对象的FCB
     */
    public static void deleteTR(FCB fcb) {
        node parentNode = searchUpperNode(fcb); //得到要删除的节点的父节点(一定是目录)
        node targetNode = selectTR(fcb);//得到要删除的节点本身

        if (parentNode == null) {
            return;
        }

        if (targetNode == null) {
            return;
        }

        if (Objects.equals(parentNode.fcb.getPathName(), "/:")) { //安全判断
            log.warn("根目录下的8个节点不能删除!");
            alertUser("小子, 你在玩火!");
            return;
        }

        // 如果要删除的节点是其父节点的左孩子
        if (parentNode.left == targetNode) {
            parentNode.left = targetNode.right; // 将父节点的左孩子指针指向要删除节点的右兄弟
        } else {
            // 如果要删除的节点是其兄弟节点的右兄弟
            node siblingNode = parentNode.left;
            while (siblingNode != null && siblingNode.right != targetNode) {
                siblingNode = siblingNode.right; // 找到要删除节点的左兄弟
            }
            if (siblingNode != null) {
                siblingNode.right = targetNode.right; // 将其兄弟节点的右兄弟指针指向要删除节点的右兄弟
            }
        }

        targetNode.left = null; // 清除要删除节点的左孩子指针
        targetNode.right = null; // 清除要删除节点的右兄弟指针

    }


    //修改TR节点
    public static void alterTR(FCB fcb1, FCB fcb2) {
        //? 拿到FCB1后, 通过String切分判断其位置, 定位到地点后执行孩子兄弟树的修改节点操作
        //? 直接替换内容也可
        //! 不能删除根目录下的8个节点!


    }


    //针对某一节点生成TR序列
    public static String pathTR(node target) {
        //? 使用StringBuilder拼接从根目录root节点到target节点的路径


        return null;
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
    static node searchNodebyOrder(node root, String[] dir, int index, StringBuilder sb) {
        //从根节点的左子树开始遍历, 每次遍历到一个节点, 就判断其是否是目录数组中的目录, 如果是, 就将tempnode指向其左子树, 如果不是, 就将tempnode指向其右子树
        //如果遍历到最后一个目录, 就将tempnode指向其左子树, 然后将其右子树指向新节点
        if (root == null) {
            return null;
        }

        if (getName(root.fcb).equals(dir[index])) { //比对当前节点的fcb的pathName切分后提取出的文件名和目录数组中的目录是否相同
            // 如果这是目录数组的最后一个元素, 定位到了, 则返回当前节点
            if (index == dir.length - 1) {
                sb.append(root.fcb.pathName.split(":")[1]).append("/"); //将当前root指向的fcb的文件名拼接到sb中
                return root;
            }

            // 否则，还得继续在左子树中搜索下一个目录
            else {
                sb.append(root.fcb.pathName.split(":")[1]).append("/"); //将当前root指向的fcb的文件名拼接到sb中
                return searchNodebyOrder(root.left, dir, index + 1, sb);
            }
        }

        // 在右子树中搜索
        return searchNodebyOrder(root.right, dir, index, sb);
    }


    /**
     * 查找给定fcb的对应节点的父节点(上一个文件层级
     *
     * @param fcb 给定的fcb
     * @return 返回父节点
     */
    public static node searchUpperNode(FCB fcb) {

        //1. 拿到FCB按照"目录 : 名称"切分为两块: 目录和名称
        String[] dir = getPathArray(fcb);
        String name = getName(fcb);
        node temp_node = fileSyS.tree.root.left;      //创建游标指针tempnode去TR中跟踪序列, 指向根节点的左子树, 也就是第一个挂载到/的节点
        StringBuilder sb = new StringBuilder();     //创建SB存储目录序列


        //2. 遍历目录数组, 每次遍历到一个目录, 就判断其是否是tempnode的左子树, 如果是, 就将tempnode指向其左子树, 如果不是, 就将tempnode指向其右子树
        node dir_temp = searchNodebyOrder(temp_node, dir, 0, sb); //得到要新增的节点的父节点

        if (dir_temp == null) {
            log.warn("文件树层级关系错误, 请检查文件系统树形结构");
            alertUser("Ooops! 系统中找不到对应的目录");
            return null;
        }

        //System.out.println(sb); //展示搜索到的目录序列
        //System.out.println(dir_temp);//展示当前找到的插入目标文件夹
        return dir_temp;
    }


    /**
     * 查询TR节点 -> 得到对应node
     *
     * @param fcb 给定的fcb
     * @return 返回对应的node
     */
    public static node selectTR(FCB fcb) {
        //? 拿到FCB后, 通过String切分判断其位置(树上意义无意义),因此暂时直接调用pathTR返回类似order的路径

        //1. 拿到FCB按照"目录 : 名称"切分为两块: 目录和名称
        String[] dir = getPathArray(fcb);
        String name = getName(fcb);

        //将dir和name合并
        String[] dir_name = new String[dir.length + 1];
        System.arraycopy(dir, 0, dir_name, 0, dir.length);
        dir_name[dir.length] = name;


        node temp_node = fileSyS.tree.root.left;      //创建游标指针tempnode去TR中跟踪序列, 指向根节点的左子树, 也就是第一个挂载到/的节点
        StringBuilder sb = new StringBuilder();     //创建SB存储目录序列


        //2. 遍历目录数组, 每次遍历到一个目录, 就判断其是否是tempnode的左子树, 如果是, 就将tempnode指向其左子树, 如果不是, 就将tempnode指向其右子树
        node dir_temp = searchNodebyOrder(temp_node, dir_name, 0, sb); //得到要新增的节点

        if (dir_temp == null) {
            log.warn("文件树层级关系错误, 请检查文件系统树形结构");
            alertUser("Ooops! 系统中找不到对应的目录");
            return null;
        }

        return dir_temp;
    }





    //! 2. 路径管理器PM


    /**
     * 初始化PM
     * <p>挂载根目录</p>
     * @deprecated 已经通过磁盘模块初始化完成
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
        //!合法性校验: 禁止删除根目录和根目录下的节点

        if (pathName.equals("/")) {
            log.warn("根目录不能删除!");
            return null;
        }

        for (ROOT_PATH root_path : ROOT_PATH.values()) {
            if (pathName.equals("/" + root_path.getName())) {
                log.warn("根目录下的8个节点不能删除!");
                return null;
            }
        }

        List<Integer> keys = fileSyS.pathManager.entrySet().stream() //将Map转换为Stream，过滤出值等于目标值的键值对，映射为键，收集为集合
                .filter(entry -> entry.getValue().equals(pathName))
                .map(Map.Entry::getKey)
                .toList();

        if (keys.isEmpty()) {
            log.warn("路径管理器中找不到对应的路径{}?!", pathName);
            return null;
        }

        fileSyS.pathManager.put(keys.get(0), "");
        return keys.get(0);
    }

    //TODO ALter


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

    //默认不提供其他修改扩展名方法. 这件事由卖电脑的家伙决定!(哈哈哈)
}
