package css.out.file.api;

import css.out.file.FileApp;
import org.junit.Test;

import java.util.Arrays;

import static css.out.file.api.MonitorApiList.diskUsageAmount_All;
import static css.out.file.api.MonitorApiList.diskUsageAmount_SyS;
import static css.out.file.api.PowerApiList.startFile;
import static css.out.file.api.TranApiList.giveBlockStatus2Front;
import static css.out.file.api.TranApiList.givePath2Front;

public class ApiTest {


    //!使用API接口Test
    @Test
    public void testFileSystemAPI() {
        startFile();
        //展示系统占用情况
        System.out.println(diskUsageAmount_All());
        System.out.println(diskUsageAmount_SyS());



    }

    @Test
    public void handleTest() {
        FileApp app = new FileApp();
//        app.kickDiskRoboot();
//        app.coverDiskRoboot();
        System.out.println(Arrays.toString(givePath2Front()));
        System.out.println(giveBlockStatus2Front());
    }
}
