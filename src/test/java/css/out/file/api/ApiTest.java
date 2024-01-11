package css.out.file.api;

import css.out.file.FileApp;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import org.junit.Test;

import java.util.Arrays;

import static css.out.file.api.FunctionApiList.handleCommon;
import static css.out.file.api.MonitorApiList.diskUsageAmount_All;
import static css.out.file.api.MonitorApiList.diskUsageAmount_SyS;
import static css.out.file.api.PowerApiList.startFile;
import static css.out.file.api.TranApiList.giveBlockStatus2Front;
import static css.out.file.api.TranApiList.givePath2Front;
import static css.out.file.entiset.GF.DIR_NAME_DEFAULT;
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
    public void CommonApiTest() {
        System.out.println("test交互式文件-进程传递");

        //使用自己的Java文件对象完成一次Common交互流程
        file test_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + str2Path(DIR_NAME_DEFAULT) + ':' + "狂人日记", FILE_EXTEND.get(1), "I \nwant to\n surpass humanity\nwith\n your blood!\n");
        handleCommon(test_file.fcb.getPathName(), test_file.fcb.getExtendName(), test_file.getContent());

    }
}
