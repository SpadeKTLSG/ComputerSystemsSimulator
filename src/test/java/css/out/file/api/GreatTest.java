package css.out.file.api;

import css.out.file.FileApp;
import css.out.file.entity.dir;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import org.junit.Test;

import static css.out.file.FileApp.addContent;
import static css.out.file.entiset.GF.DIR_EXTEND;
import static css.out.file.entiset.GF.FILE_EXTEND;
import static css.out.file.handleB.HandleFile.str2Path;

public class GreatTest {

    @Test
    public void workWithFront() {

        //!正确打开方式: 先运行Main.java, 再运行GreatTest.java
     /*   invokeLater(() -> {
            MainGui gui = new MainGui();
            gui.showGUI();
        });*/
//        Main.main(null);
//        msg2Front("Hello World!");

        FileApp app = new FileApp();
        file test_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Crazy", FILE_EXTEND.get(1), "I want to  surpass humanity -- with your blood!");
        dir test_dir = new dir(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Guys", DIR_EXTEND.get(0));
        addContent(test_file);
        addContent(test_dir);
//        getFrontRequest("create Crazy.txt /tmp");

        //? 1. 测试命令接口

        //? 2. 测试硬盘传递

        //? 3. 测试文件树传递
    }

}
