package css.out.file.entiset;

import css.out.file.entity.disk;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

    /**
     * 单例实现
     */
    public DiskSyS() {
        this.disk = new disk(); //赋值成员变量空间
    }

    public void reloadDiskSyS() {

    }

    @Override
    public String toString() {
        return "DiskSyS{" +
                "disk=" + disk +
                '}';
    }
}