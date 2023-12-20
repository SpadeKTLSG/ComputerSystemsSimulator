package css.out.file.handle;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static css.out.file.entity.GlobalField.DISK_FILE;
import static css.out.file.entity.GlobalField.WORKSHOP_PATH;

/**
 * 处理外置磁盘TXT相关工具类
 */
@Slf4j
public abstract class HandleDISK {

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
    public static void writeDISK(List<block> BLOCKS, String path) {

        File diskFile = new File(path);
        if (!diskFile.exists()) {
            // 如果不存在抛出异常
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                log.error("创建磁盘映射文件失败, 错误日志: {}", e.getMessage());
            }
        }


        try (FileOutputStream fis = new FileOutputStream(diskFile)) {

//            for (block block : BLOCKS) { //block是磁盘块阵列中的每一个磁盘块, 由字节组成
//                System.out.println(block);
//                //!要求DISK.BLOCKSList中的所有项一行一个块写入, 块中的64个字节全部写入目标TXT文件,空的字节用0填充
//                //FIXME
//                //写入一个块的字节流序列
//                fis.write(block.bytes);
//                fis.flush(); //刷新缓冲区
//            }
        } catch (Exception e) {
            log.error("写入磁盘映射文件错误日志: {}", e.getMessage());
        }

        log.debug("写入磁盘映射文件 {} 成功", path);
    }

    /**
     * 从目标TXT文件中读取磁盘对象DISK.BLOCKS全部内容
     *
     * @param BLOCKS 磁盘块阵列
     * @param path   目标TXT文件路径
     */
    public static void readDISK(List<block> BLOCKS, String path) {

        File diskFile = new File(path);
        if (!diskFile.exists()) {
            // 如果不存在抛出异常
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                log.error("创建磁盘映射文件失败, 错误日志: {}", e.getMessage());
            }
        }

        try (FileInputStream fis = new FileInputStream(diskFile)) {
            log.info("读取磁盘映射文件 {} 中", path);
            for (block block : BLOCKS) { //block是磁盘块阵列中的每一个磁盘块, 由字节组成

                //!要求DISK.BLOCKSList中的所有项一行一个块读取, 块中的64个字节全部读到磁盘块中

                //TODO 读取一个块的字节流序列
//                Byte byteStream= (byte) fis.read(); //读取一个块的字节流序列
//                block = new block(block.getBlockByteBuilder(byteStream));
            }

        } catch (Exception e) {
            log.error("读取磁盘映射文件{}失败, 错误日志: {}", path, e.getMessage());
        }

        log.info("读取磁盘映射文件 {} 成功", path);
    }
}
