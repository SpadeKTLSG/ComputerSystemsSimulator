package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;

import static css.out.file.utils.GlobalField.*;
import static css.out.file.utils.HandlePath.getROOT_DIRPath;

/**
 * 二叉树树形文件结构, 用于存储文件系统结构
 * <p>TREE特指文件系统树形结构</p>
 */
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
     * 初始化文件系统树形结构
     */
    public TREE() {
        this.name = FILE_TREE_NAME;
        this.root = new node(ROOT_AUTH); //挂载根节点

    }

    /**
     * 挂载根目录到树上, 需要对应根目录的FCB
     *
     * @param root 根目录
     */
    public void mountROOT_DIR(node root) {
//        for (String i : ROOT_PATH) {
//            String path = getROOT_DIRPath(i);
//        }


    }

    //挂载各个根目录下的预设目录到根目录上, 左子树为第一个目录, 右子树为其第二个目录, 以此类推
}
