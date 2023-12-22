package css.out.file;

import org.junit.Test;

import static css.out.file.entiset.GF.ROOT_AUTH;

public class AppTest {

    @Test
    public void AppGreatTest() {
//        FileApp app = new FileApp(); //开机
//        app.state();
//        app.reload();//刷新
//        app.state();
//        app.reboot();//重启
//        app.state();

        FileApp app0 = new FileApp(ROOT_AUTH, 1); //格式化磁盘
    }

    @Test
    public void AppCoworkTest() {

    }

    @Test
    public void DiskSySTest() {

    }

    @Test
    public void FileSySTest() {

    }
}
