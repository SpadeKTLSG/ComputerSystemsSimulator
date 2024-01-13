package css.out.file.api;

import org.junit.Test;

import static css.front.api.request.msg2Front;

public class GreatTest {

    @Test
    public void workWithFront() {

        //!正确打开方式: 先运行Main.java, 再运行GreatTest.java
     /*   invokeLater(() -> {
            MainGui gui = new MainGui();
            gui.showGUI();
        });*/
//        Main.main(null);
        msg2Front("Hello World!");

        //? 1. 测试命令接口

        //? 2. 测试硬盘传递

        //? 3. 测试文件树传递
    }

}
