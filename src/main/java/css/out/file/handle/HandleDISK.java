package css.out.file.handle;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static css.out.file.entity.GlobalField.DISK_FILE;
import static css.out.file.entity.GlobalField.WORKSHOP_PATH;

/**
 * 处理外置磁盘TXT相关工具类
 */
@Slf4j
public abstract class HandleDISK {

    /**
     * 将msg直接破坏性注入磁盘映射文件
     * <p>这样我们就真的回不到过去了, 前辈</p>
     *
     * @param msg 要写入的字符串
     */
    public static void writeStr2Disk(String msg) {
        String path = WORKSHOP_PATH + DISK_FILE; //路径为: common/file/disk.txt
        File diskFile = new File(path);

        //将msg转换为Bytes[]后写入
        try (FileOutputStream fos = new FileOutputStream(diskFile)) {
            fos.write(msg.getBytes(StandardCharsets.UTF_8));
            fos.flush();
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

        File diskFile = new File(path);
        if (!diskFile.exists()) {
            // 如果不存在抛出异常
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                log.error("创建磁盘映射文件失败, 错误日志: {}", e.getMessage());
            }
        }


        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(WORKSHOP_PATH + DISK_FILE), StandardCharsets.UTF_8))) {
            //将BLOCKS中的每个磁盘块的内容一个一个写入
            //TODO 抽取
            for (block block : BLOCKS) {

                Byte[] byte_temp = block.bytes;
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

        log.debug("写入磁盘映射文件 {} 成功", path);
    }

    /**
     * 从目标TXT文件中读取磁盘对象DISK.BLOCKS全部内容
     * <p>存入一整个String大对象中</p>
     *
     * @param BLOCKS 磁盘块阵列
     * @param path   目标TXT文件路径
     */
    public static String readAllDISK(List<block> BLOCKS, String path) {

        File diskFile = new File(path);


        if (!diskFile.exists()) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                log.error("创建磁盘映射文件失败, 错误日志: {}", e.getMessage());
            }
        }
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(WORKSHOP_PATH + DISK_FILE), StandardCharsets.UTF_8))) {
            log.info("读取磁盘映射文件 {} 中", path);
            // 把每一行的内容存储到builder中


            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }


        } catch (Exception e) {
            log.error("读取磁盘映射文件{}失败, 错误日志: {}", path, e.getMessage());
        }


        log.info("读取磁盘映射文件 {} 成功", path);
        return sb.toString();
    }


}
