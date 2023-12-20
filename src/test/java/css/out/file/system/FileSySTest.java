package css.out.file.system;

import css.out.file.FileApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static css.out.file.handle.HandlePath.printTree;
import static css.out.file.utils.TreeUtil.showGreatTree;

@Slf4j
public class FileSySTest {

    @Test
    public void initialSySTest() {
        FileApp app = new FileApp();
        System.out.println("文件系统树形结构初始化效果");
        printTree(FileApp.fileSyS.tree.root);
        showGreatTree(FileApp.fileSyS.tree.root);

    }
}
