package css.out.file.entity;

import css.out.file.FileApp;
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
        FileApp app = new FileApp();
        FCB f = new FCB("/home", ROOT_DIR_BLOCK, FILE);
        file file1 = new file(new FCB("/home", 114, FILE_EXTEND.get(0), FILE, FILE_LENGTH_DEFAULT + FCB_BYTE_LENGTH + 1), "114514");
        System.out.println(file1);
        dir dir1 = new dir(new FCB("/home", ROOT_DIR_BLOCK, DIR_EXTEND, DIR, DIR_LENGTH_DEFAULT + FCB_BYTE_LENGTH));

        f.toBytes();
    }


    /**
     *
     */
    @Test
    public void test() {
        //创建一个字节的变量
        byte b = 0x01;
        //创建一个字节大小的byte[]
        byte[] bytes = new byte[1];

//        System.out.println(FCB_LENGTH.keySet());
//        System.out.println(FCB_LENGTH.get("pathName"));
//        System.out.println(FCB_LENGTH.get("startBlock"));
//        System.out.println(FCB_LENGTH.get("typeFlag"));
//        System.out.println(FCB_LENGTH.get("fileLength"));

    }
}
