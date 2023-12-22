package css.out.file;

import org.junit.Test;

import static css.out.file.entiset.GF.ROOT_AUTH;

public class AppTest {

    @Test
    public void AppGreatTest() {
        FileApp app = new FileApp(); //开机
//        app.state();
        app.reload();//刷新
//        app.state();
        app.reboot();//重启
        app.state();


    }

    @Test
    public void AppCoworkTest() {
        FileApp app = new FileApp(); //开机
        app = new FileApp(ROOT_AUTH, 1); //格式化磁盘, 去除磁盘污染
        app.state();
        app = new FileApp(ROOT_AUTH, 2); //覆盖磁盘, 同步状态
        app.state();
    }

    @Test
    public void DiskSySTest() {

    }

    @Test
    public void FileSySTest() {

    }
}
