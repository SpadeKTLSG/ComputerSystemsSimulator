package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.entity.GlobalField.ROOT_DIR_BLOCK;
import static css.out.file.utils.TreeUtil.showGreatTree;

/**
 * 二叉树树形文件结构, 用于存储文件系统结构
 * <p>TREE特指文件系统树形结构</p>
 */
@Slf4j
public class TREE {

    //使用树的二叉树表示法, 根节点为/根目录, 其只有一个孩子节点, 为根目录下的第一个文件夹

    /**
     * 根节点不绑定FCB, 其下的8个孩子节点绑定FCB
     */
    public node root;

    /**
     * 树形文件结构名称
     */
    public String name;

    /**
     * 初始化文件系统树形结构(单例)
     */
    public TREE() {

    }

    /**
     * 挂载根目录到树上, 需要对应根目录的FCB
     */
    public void mountROOT_DIR() {
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
//        log.debug("包含根目录的文件结构树初始化完毕");
    }

    @Override
    public String toString() {
        //直接调用方法把树形结构打印出来
        //printTree(root);
        return showGreatTree(root);
    }
}
