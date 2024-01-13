package css.front;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.Dimension;

public class DynamicTreeExample {
    JTree pathTree;
    private DefaultTreeModel treeModel;

    public DynamicTreeExample() {
        // 创建根节点
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        // 创建树
        treeModel = new DefaultTreeModel(root);
        pathTree = new JTree(treeModel);

        // 设置树的首选大小
        pathTree.setPreferredSize(new Dimension(560, 200));


    }

    // 更新树状路径的方法
    public void updateTree(String[] pathArray) {
        // 获取根节点
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();

        // 清空树
        root.removeAllChildren();
        treeModel.reload();

        // 动态添加节点
        DefaultMutableTreeNode currentNode = root;
        for (String pathPart : pathArray) {
            String[] subdirectories = pathPart.split("/");
            for (String subdirectory : subdirectories) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(subdirectory);
                currentNode.add(newNode);
                currentNode = newNode;
            }
        }

        // 刷新树
        treeModel.reload();
    }

    // 获取选定节点的路径
    public TreePath getSelectedPath() {
        return pathTree.getSelectionPath();
    }


}
