package css.out.file.entiset;

import css.out.file.entity.TREE;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例实现文件系统
 */
@Data
public class FileSyS {

    /**
     * 文件系统树形结构
     * <p>没有存在磁盘</p>
     */
    public TREE tree;

    /**
     * 路径管理器
     * <p>没有存在磁盘</p>
     */
    public Map<Integer, String> pathManager;

    /**
     * 扩展名管理器
     * <p>没有存在磁盘</p>
     */
    public Map<Integer, String> extendManager;

    /**
     * 单例实现
     */
    public FileSyS() {
        this.tree = new TREE(); //赋值成员变量空间
        this.extendManager = new HashMap<>(); //赋值成员变量空间
        this.pathManager = new HashMap<>(); //赋值成员变量空间
    }

    @Override
    public String toString() {
        return "FileSyS{" +
                "tree=" + tree +
                "\npathManager=" + pathManager +
                "\nextendManager=" + extendManager +
                "}\n";
    }
}
