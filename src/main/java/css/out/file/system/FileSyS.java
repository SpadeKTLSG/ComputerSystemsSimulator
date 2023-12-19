package css.out.file.system;

import css.out.file.entity.TREE;
import lombok.Data;

/**
 * 单例实现文件系统
 */
@Data
public class FileSyS {

    /**
     * 文件系统树形结构
     */
    public TREE tree;


    public Integer id;

    public FileSyS() {

        //TODO 根节点特殊处理, 创建8个初始文件夹
    }


}