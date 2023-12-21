package css.out.file.entity;

import css.out.file.FileApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

import static css.out.file.entity.GF.*;
import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.handle.HandleFile.setFileContextLength;

/**
 * 文件&文件夹测试
 */
@Slf4j
public class FileDirTest {

    @Test
    public void FileDirFCB_Build() {
        FileApp app = new FileApp();

        //测试对象
//        FCB f = new FCB("/home", ROOT_DIR_BLOCK, FILE);
        file file1 = new file(new FCB("/home", 89, FILE_EXTEND.get(1), FILE, FILE_LENGTH_DEFAULT + FCB_BYTE_LENGTH + setFileContextLength("114514")), "114514");
//        System.out.println(file1);
//        file1.fcb.setFileLength(file1.fcb.getFileLength() + getFileContextLength(file1));


        dir dir1 = new dir(new FCB("/home", 112, DIR_EXTEND.get(0), DIR, DIR_LENGTH_DEFAULT + FCB_BYTE_LENGTH));
//        System.out.println(dir1);

        //转换操作
        byte[] byte_temp = file1.toBytes();
        //foreach 打印 byte_temp中的每个元素
//        for (byte b : byte_temp) {
//            System.out.print(b + " ");
//        }

        //!用StringBuffer收集byte_temp中的每个元素,最后打印SB
        StringBuffer sb = new StringBuffer();
        for (byte b : byte_temp) {
            sb.append(b).append(" ");
        }
        System.out.println(sb);
        String res = sb.toString();
        System.out.println(res);
        System.out.println();
        System.out.println(Arrays.toString(byte_temp));
        dir temp_dir = new dir();
        temp_dir.fromBytes(byte_temp);
//        System.out.println(temp_dir);

    }


}
