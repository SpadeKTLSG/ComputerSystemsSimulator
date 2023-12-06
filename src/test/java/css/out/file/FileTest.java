package css.out.file;

import org.junit.Test;


public class FileTest {
    @Test
    public void testFileSystem() {
        //模拟文件系统, 输出一段随机字符串
        String SthFromFileSystem = "Word很大, 你要忍一下";
        System.out.println("testFileSystem, output: " + SthFromFileSystem);

    }
}
