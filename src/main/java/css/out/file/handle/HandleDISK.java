package css.out.file.handle;

import css.out.file.FileApp;
import css.out.file.entity.block;
import css.out.file.entity.disk;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static css.out.file.entiset.GF.*;
import static css.out.file.handle.HandleBlock.*;
import static css.out.file.utils.ByteUtil.str2Byte;

/**
 * I级 磁盘(TXT)管理工具类
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
            for (block block : BLOCKS) {

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
//            log.debug("读取磁盘映射文件 {} 中", path);


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
     * 开机执行, 不破坏TXT文件的情况下, 从TXT文件中读取磁盘对象DISK.BLOCKS的流式全部内容
     *
     * <p>此时DiskSyS还没有生成</p>
     */
    public static void initialDisk(disk disk) {
        disk.name = DISK_NAME;
        disk.BLOCKS = getDefaultBLOCKS(); //获得磁盘空间
        disk.FAT1 = getDefaultFAT1(); //获得FAT1对象

        disk.FAT2 = getDefaultFAT2(); //获得FAT2对象
    }

    /**
     * 完全重新格式化磁盘
     * <p>重新格式化磁盘, 会清空磁盘中的所有数据</p>
     */
    public static void totalReloadDisk() {
        FileApp.diskSyS.disk.name = DISK_NAME;
        FileApp.diskSyS.disk.BLOCKS = getDefaultBLOCKS(); //获得磁盘空间

        FileApp.diskSyS.disk.FAT1 = getDefaultFAT1(); //获得FAT1对象
        byte[] FAT1_Byte = getFATBytes(FileApp.diskSyS.disk.FAT1); //获得FAT1字节对象
        mountFAT(FileApp.diskSyS.disk.BLOCKS, FAT1_Byte, 1); //挂载FAT1字节对象

        FileApp.diskSyS.disk.FAT2 = getDefaultFAT2(); //获得FAT2对象
        byte[] FAT2_Byte = getFATBytes(FileApp.diskSyS.disk.FAT2); //获得FAT2字节对象
        mountFAT(FileApp.diskSyS.disk.BLOCKS, FAT2_Byte, 2); //挂载FAT2字节对象

        writeAllDISK(FileApp.diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE); //写入磁盘
        log.debug("{}初始化完成!", FileApp.diskSyS.disk.name);
    }

    /**
     * 正常从磁盘完全加载磁盘系统
     */
    public static void normalRebootDisk() {
        reloadStr2Disk(readAllDISK(WORKSHOP_PATH + DISK_FILE));
        //通信...
    }

    /**
     * 从磁盘映射文件中读取磁盘对象
     *
     * @param great_str 磁盘映射文件长字符串
     */
    public static void reloadStr2Disk(String great_str) {

        String[] str = great_str.split("\n");


        for (int i = 0; i < DISK_SIZE; i++) {

            if (i == FAT1_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                FileApp.diskSyS.disk.FAT1 = fromFATBytes(bytes_temp);
                mountFAT(FileApp.diskSyS.disk.BLOCKS, bytes_temp, 1);
                continue;
            }

            if (i == FAT2_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                FileApp.diskSyS.disk.FAT2 = fromFATBytes(bytes_temp);
                mountFAT(FileApp.diskSyS.disk.BLOCKS, bytes_temp, 2);
                continue;
            }

            byte[] bytes_temp = str2Byte(str[i]);
            setSingleBLOCKS(bytes_temp, i);
        }

        writeAllDISK(FileApp.diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE); //写入磁盘
        log.debug("{}初始化完成!", FileApp.diskSyS.disk.name);
    }


}
