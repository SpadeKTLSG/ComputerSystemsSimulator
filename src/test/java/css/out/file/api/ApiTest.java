package css.out.file.api;

import css.out.file.FileApp;
import css.out.file.entity.dir;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import org.junit.Test;

import java.util.Arrays;

import static css.out.file.FileApp.addContent;
import static css.out.file.api.MonitorApiList.diskUsageAmount_All;
import static css.out.file.api.MonitorApiList.diskUsageAmount_SyS;
import static css.out.file.api.PowerApiList.startFile;
import static css.out.file.api.TranApiList.*;
import static css.out.file.entiset.GF.DIR_EXTEND;
import static css.out.file.entiset.GF.FILE_EXTEND;
import static css.out.file.handleB.HandleFile.str2Path;

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

    @Test
    public void CommonApiTest1() {
        System.out.println("test交互式文件-进程传递");
        FileApp app = new FileApp();
        //使用自己的Java文件对象完成一次Common交互流程
        file test_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Crazy", FILE_EXTEND.get(1), "I want to  surpass humanity -- with your blood!");
        dir test_dir = new dir(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Guys", DIR_EXTEND.get(0));
        addContent(test_file);
//        addContent(test_dir);
//        app.state();

        //模拟前端请求
//        getFrontRequest("create Crazy.txt /tmp");
        getFrontRequest("copy Crazy.txt /tmp /home");
        app.state();
    }
}
