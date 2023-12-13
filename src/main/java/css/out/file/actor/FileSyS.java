package css.out.file.actor;

import css.out.file.entity.TREE;
import lombok.Data;

/**
 * 文件系统
 * TODO 调整可见性
 */
@Data
public class FileSyS {

    /**
     * 文件系统树形结构
     */
    public TREE tree;

    public FileSyS() {

        //TODO 根节点特殊处理, 创建8个初始文件夹
    }

    //文件系统找到磁盘系统执行命令
    public void tell() {
        DiskSyS diskSyS = new DiskSyS();
    }

}
