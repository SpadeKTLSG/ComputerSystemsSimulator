package css.out.file.entity;

import css.out.file.FileApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.api.CommonApiList.alertUser;
import static css.out.file.entiset.GF.*;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.handleB.HandleDISK.*;
import static css.out.file.handleB.HandleFile.*;
import static css.out.file.handleB.HandleTXT.writeAllDISK2TXT;

/**
 * 文件&文件夹测试
 */
@Slf4j
public class DiskTest {


    /**
     * 磁盘搭载与交互测试
     */
    @Test
    public void Disk_Interact() throws IOException {
        FileApp app = new FileApp();

        file file1 = new file(new FCB("/home", 89, FILE_EXTEND.get(1), FILE, FILE_LENGTH_DEFAULT + FCB_BYTE_LENGTH + setFileContextLength("114514")), "114514");
        System.out.println(file1);
        //转换操作
        byte[] byte_temp = file2Bytes(file1);

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
        bw.newLine();
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
        br.readLine();
        // 读取一行的内容，返回一个字符串, 并跳到下一行开始
        String line = br.readLine();
        // 打印到控制台
        System.out.println(line);
        // 关闭BufferedReader对象
        br.close();


        //将读取到的line转回对象
        //1. 将line按照空格分割为Bytes[]
        String[] str = line.split(" ");
        //2. 将Bytes[]转换为byte[]
        byte[] bytes = new byte[str.length];
        for (int i = 0; i < str.length; i++) {
            bytes[i] = Byte.parseByte(str[i]);
        }
        //3. 将byte[]转换为file对象

        file temp_file = bytes2File(bytes);
        System.out.println(temp_file);
    }

    /**
     * 磁盘读取存入测试
     */
    @Test
    public void Disk_function1() throws IOException {
        FileApp app = new FileApp();

        //尝试设置一个文件
        //处理FAT磁盘逻辑
//        fullFillFAT(1); //FAT1满
//        fullFillFAT(2); //全满
        Integer pos = get1FreeBlock();
        if (pos != -1) {
            System.out.println(pos + "是被找到的第一个空闲块");
            //写入磁盘
            writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        }
        List<Integer> order = getFATOrder();
        System.out.println(order);

        log.warn("FAT1和FAT2都装不下咯!, 当前FAT状态: FAT1: {}, FAT2: {}", diskSyS.disk.FAT1, diskSyS.disk.FAT2);
        alertUser("磁盘被撑爆了, Behave yourself!");

//        app.kickDiskRoboot();
        app.state();

        //设置一个盘块占用了

    }


    /**
     * 磁盘搭载测试
     */
    @Test
    public void Disk_reload() throws IOException {

    }
}
