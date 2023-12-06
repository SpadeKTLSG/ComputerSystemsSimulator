package css.out.file.api;

import css.out.file.test;


public class FileApiList {
//    这是文件系统唯一暴露给外界的接口文件, 如果想要与我通信, 请访问我而不是他们

    //示例: 让我输出一段文字, 同时打印到日志
    //调用 css/out/file/test.java
    public String FileApiListTest() {
        test test = new test();
        String StorageData = test.testFileSystem();

        //传递到前端显示
        StorageData = "前端输出:  " + StorageData;
        return StorageData;
    }
}
