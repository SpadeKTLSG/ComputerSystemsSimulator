package css.out.file.system;

import css.out.file.FileApp;
import css.out.file.entity.disk;
import lombok.Data;

/**
 * 单例实现磁盘系统
 */
@Data
public class DiskSyS {

    /**
     * 磁盘
     */
    public disk disk;

    public DiskSyS() {
        this.disk = new disk();

    }

    public void reloadDiskSyS() {
        this.disk.reload();
    }
}
