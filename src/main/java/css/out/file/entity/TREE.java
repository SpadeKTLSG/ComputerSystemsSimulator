package css.out.file.entity;

/**
 * 二叉树树形文件结构, 用于存储文件系统结构
 * <p>TREE特指文件系统树形结构</p>
 */
public class TREE {

    //使用树的二叉树表示法, 根节点为/根目录, 其只有一个孩子节点, 为根目录下的第一个文件夹

    /**
     * ?根节点不绑定FCB, 其下的8个孩子节点绑定FCB
     */
    public node root;


}
