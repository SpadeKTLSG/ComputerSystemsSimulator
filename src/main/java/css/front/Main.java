package css.front;

import css.out.file.entity.dir;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.FileApp.addContent;
import static css.out.file.api.PowerApiList.startFile;
import static css.out.file.entiset.GF.DIR_EXTEND;
import static css.out.file.entiset.GF.FILE_EXTEND;
import static css.out.file.handleB.HandleFile.str2Path;

@Slf4j
public class Main {
    public static void main(String[] args) {
        // 运行图形化主界面线程
        startFile();
        file test_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Crazy", FILE_EXTEND.get(1), "I want to  surpass humanity -- with your blood!");
        dir test_dir = new dir(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Guys", DIR_EXTEND.get(0));
        addContent(test_file);
        addContent(test_dir);
        javax.swing.SwingUtilities.invokeLater(() -> {
            // 创建图形化界面
            MainGui gui = new MainGui();
            log.info("启动界面");
            // 设置面板可见
            gui.showGUI();
        });
    }
}
