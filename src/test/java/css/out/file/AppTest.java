package css.out.file;

import org.junit.Test;

public class AppTest {

    @Test
    public void AppGreatTest() {
        FileApp app = new FileApp(); //开机
        app.state();
        app.reload();//刷新
        app.state();
        app.reboot();//重启
        app.state();
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
