package css.out.file.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.utils.GlobalField.*;

/**
 * 文件&文件夹测试
 */
@Slf4j
public class FileDirTest {

    /**
     * 测试FCB构建
     */
    @Test
    public void FileDirFCB_Build() {
/*        log.info("FCB构建测试");
        FCB fcb1 = new FCB("/home", ROOT_DIR_BLOCK, FILE);
//        System.out.println(fcb1.toString());
        FCB fcb2 = new FCB("/home", ROOT_DIR_BLOCK, DIR);
//        System.out.println(fcb2.toString());
        FCB fcb3 = new FCB("/home", ROOT_DIR_BLOCK, FILE_EXTEND.get(0), FILE, FILE_LENGTH_DEFAULT + FCB_BYTE_LENGTH + 1);
//        System.out.println(fcb3.toString());
        FCB fcb4 = new FCB("/home", ROOT_DIR_BLOCK, DIR_EXTEND, DIR, DIR_LENGTH_DEFAULT + FCB_BYTE_LENGTH);
//        System.out.println(fcb4.toString());*/
        log.info("文件与文件夹构建测试");
        file file1 = new file(new FCB("/home", ROOT_DIR_BLOCK, FILE_EXTEND.get(0), FILE, FILE_LENGTH_DEFAULT + FCB_BYTE_LENGTH + 1), "114514");
        System.out.println(file1);
        dir dir1 = new dir(new FCB("/home", ROOT_DIR_BLOCK, DIR_EXTEND, DIR, DIR_LENGTH_DEFAULT + FCB_BYTE_LENGTH));
        System.out.println(dir1);
        file file2 = new file("114514");
        System.out.println(file2);
        dir dir2 = new dir();
        System.out.println(dir2);

    }


    /**
     * 测试文件控制块的字节数组转换方法
     */
    @Test
    public void test() {

    }
}
