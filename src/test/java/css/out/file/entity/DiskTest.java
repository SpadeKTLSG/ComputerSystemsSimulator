package css.out.file.entity;

import css.out.file.FileApp;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static css.out.file.entity.GlobalField.*;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.handle.HandleFile.setFileContextLength;

/**
 * 文件&文件夹测试
 */
public class DiskTest {


    /**
     * 磁盘搭载与交互测试
     */
    @Test
    public void Disk_Interact() throws IOException {
        FileApp app = new FileApp();

        file file1 = new file(new FCB("/home", 89, FILE_EXTEND.get(1), FILE, FILE_LENGTH_DEFAULT + FCB_BYTE_LENGTH + setFileContextLength("114514")), "114514");

        //转换操作
        byte[] byte_temp = file1.toBytes();

        //!用StringBuffer收集byte_temp中的每个元素,最后打印SB
        StringBuilder sb = new StringBuilder();
        for (byte b : byte_temp) {
            sb.append(b).append(" ");
        }
        String res = sb.toString();
        // 创建一个BufferedWriter对象，写入disk.txt文件, 指定编码为UTF-8
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(WORKSHOP_PATH + DISK_FILE), StandardCharsets.UTF_8));
        // 写入一行的内容res
        bw.write(res);
        // 换行
        bw.newLine();
        // 写入一行的内容res
        bw.write(res);
        //再写
        bw.write(res);
        bw.flush();
        bw.close();


        // 创建一个BufferedReader对象，读取disk.txt文件，指定编码为UTF-8
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(WORKSHOP_PATH + DISK_FILE), StandardCharsets.UTF_8));
        // 跳过文件的前1行, 不知道这行有多大的情况下进行:
        //跳过字符
        br.skip(0);

        // 读取一行的内容，并跳到下一行开始
        br.readLine();
        // 读取一行的内容，返回一个字符串, 并跳到下一行开始
        String line = br.readLine();
        // 打印到控制台
        System.out.println(line);
        // 关闭BufferedReader对象
        br.close();
    }

    @Test
    public void Disk_function() throws IOException {

    }
}
