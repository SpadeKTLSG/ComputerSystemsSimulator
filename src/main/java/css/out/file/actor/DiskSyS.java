package css.out.file.actor;

import css.out.file.entity.disk;
import lombok.Data;

/**
 * 磁盘系统
 *  TODO 调整可见性
 */
@Data
public class DiskSyS {

    /**
     * 磁盘
     */
    public disk disk;

    public DiskSyS() {
        this.disk = new disk(); //挂载磁盘
    }
}
