package css.out.file.handleB;

import css.out.file.entity.block;
import css.out.file.entity.disk;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.handleB.HandleBlock.*;
import static css.out.file.utils.ByteUtil.str2Byte;

/**
 * I级 磁盘(TXT)管理工具类
 */
@Slf4j
public abstract class HandleDISK {


    /**
     * 将原生String msg直接破坏性狠狠注入磁盘映射文件的对应行
     * <p>这样我们就真的回不到过去了, 前辈!</p>
     * <p>狠狠滴调教啊混蛋</p>
     *
     * @param msg  要写入的字符串
     * @param path 目标TXT文件路径
     * @param pos  位置
     */
    public static void writeStr2Disk(String msg, String path, Integer pos) {
        StringBuilder sb = new StringBuilder();
        Integer pos_temp = 0;
        //先把全部的读取保存, 然后修改对应行为自己的String msg, 再写入
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            //全部读取保存到StringBuffer
            String line;
            while ((line = br.readLine()) != null) {
                if (pos_temp.equals(pos)) {
                    sb.append(msg).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
                pos_temp++;
            }
        } catch (Exception e) {
            log.error("注入msg到{}位置失败! {}", path, e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {

            //通过sb全部写入:
            for (String line : sb.toString().split("\n")) {

                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            log.error("写入磁盘映射文件{}失败, 错误日志: {}", path, e.getMessage());
        }
    }

    /**
     * 将磁盘对象DISK.BLOCKS全部内容写入目标TXT文件
     *
     * @param BLOCKS 磁盘块阵列
     * @param path   目标TXT文件路径
     */
    public static void writeAllDISK(List<block> BLOCKS, String path) {

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {

            for (block block : BLOCKS) {//将BLOCKS中的每个磁盘块的内容一个一个写入

                byte[] byte_temp = block.bytes;
                StringBuilder sb = new StringBuilder();
                for (byte b : byte_temp) {
                    sb.append(b).append(" ");
                }
                bw.write(sb.toString());
                bw.newLine();
            }

        } catch (Exception e) {
            log.error("写入磁盘映射文件错误日志: {}", e.getMessage());
        }

//        log.debug("写入磁盘映射文件 {} 成功", path);
        log.debug("写入磁盘映射文件成功");
    }

    /**
     * 从目标TXT文件中读取磁盘对象DISK.BLOCKS的流式全部内容
     * <p>存入一整个String大对象中</p>
     *
     * @param path 目标TXT文件路径
     * @return 一整个String大对象
     */
    public static String readAllDISK(String path) {

        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (Exception e) {
            log.error("读取磁盘映射文件{}失败, 错误日志: {}", path, e.getMessage());
        }

        log.debug("读取磁盘映射文件成功");
        return sb.toString();
    }


    /**
     * 利用String磁盘内容赋值磁盘Java对象
     *
     * @param great_str 磁盘映射文件长字符串
     */
    public static void reloadStr2Disk(String great_str) {

        String[] str = great_str.split("\n");

        for (int i = 0; i < DISK_SIZE; i++) {

            if (i == FAT1_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                diskSyS.disk.FAT1 = fromFATBytes(bytes_temp);
                mountFAT(diskSyS.disk.BLOCKS, bytes_temp, 1);
                continue;
            }

            if (i == FAT2_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                diskSyS.disk.FAT2 = fromFATBytes(bytes_temp);
                mountFAT(diskSyS.disk.BLOCKS, bytes_temp, 2);
                continue;
            }

            byte[] bytes_temp = str2Byte(str[i]);
            setSingleBLOCKS(bytes_temp, i);
        }

        writeAllDISK(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE); //二次写入磁盘保证一致性
        log.debug("{}初始化完成!", diskSyS.disk.name);
    }


}
