package css.out.file.api;

import css.out.file.FileApp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PowerApiList {

    /**
     * !开始我们的战争罢!
     */
    public static void OpenTheGame() {
        FileApp app = new FileApp(); //开机 - 将FileApp类实例化到内存中
        app.state();
    }
}
