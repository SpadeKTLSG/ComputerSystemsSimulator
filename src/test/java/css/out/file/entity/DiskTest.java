package css.out.file.entity;

import css.out.file.FileApp;
import org.junit.Test;

import static css.out.file.handle.HandleDISK.writeStr2Disk;

/**
 * 文件&文件夹测试
 */
public class DiskTest {


    /**
     * 磁盘搭载
     */
    @Test
    public void Disk_Build() {
        FileApp app = new FileApp();
        System.out.println(FileApp.diskSyS);
        writeStr2Disk("hhhh");

    }
}
