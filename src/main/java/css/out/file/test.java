package css.out.file;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {
    public String testFileSystem() {
        //模拟文件系统, 输出一段随机字符串
        String SthFromFileSystem = "Word很大, 你要忍一下";
        log.info("曹文星的testFileSystem输出:  {}", SthFromFileSystem);
        return SthFromFileSystem;

    }
}
