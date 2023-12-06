package css.out.file;

import css.out.file.api.FileApiList;
import org.junit.Test;


public class FileTest {


    //!使用API接口Test
    @Test
    public void testFileSystemAPI() {
        //注入测试API接口对象
        FileApiList fileApiList = new FileApiList();
        System.out.println(fileApiList.FileApiListTest());
//        fileApiList.FileApiListTest();
    }

    //?一般Test
    @Test
    public void testFileSystem() {

    }
}
