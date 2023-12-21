package css.out.file.system;

import css.out.file.entity.disk;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.handle.HandleDISK.normalRebootDisk;

/**
 * 单例实现磁盘系统
 */
@Slf4j
@Data
public class DiskSyS {

    /**
     * 磁盘
     */
    public disk disk;

    public DiskSyS() {
        this.disk = new disk();
//        reloadDiskSyS();
    }

    public void reloadDiskSyS() {
        normalRebootDisk();
    }

    @Override
    public String toString() {
        return "DiskSyS{" +
                "disk=" + disk +
                '}';
    }
}
