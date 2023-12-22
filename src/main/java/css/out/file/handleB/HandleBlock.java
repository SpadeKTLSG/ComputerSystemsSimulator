package css.out.file.handleB;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
import static css.out.file.handleB.HandleDISK.write1Str2TXT;
import static css.out.file.utils.ByteUtil.byte2Str;
import static css.out.file.utils.ByteUtil.str2Byte;

/**
 * II级 磁盘块工具类
 */
@Slf4j
public abstract class HandleBlock {

    //! 1.磁盘块CRUD

    //TODO: 根据FAT从磁盘块中找到1个空闲的磁盘块位置
    public static int get1FreeBlock() {

        //在全局的FAT中查找第一个值为514的位置, 同时将FAT最晚的一个块指向这个位置, 这个位置同样指向空(514)
        //返回这个位置的块号

        //对象: diskSyS.disk.FAT1 + FAT2, 从开始盘块:全局变量FAT1开始指针查找, 将对应位置的值作为下一个访问的位置, 直到读取到514代表空; 而后写入新的对象
        //如果FAT1遍历完了, 仍然没有找到空闲块, 则去FAT2查找, 仍然没有找到就报错
        int pos = FAT1_DIR; //从FAT1开始
        for (int i = 0; i < FAT_SIZE; i++) {
            if (diskSyS.disk.FAT1.get(pos) == Null_Pointer) {
                diskSyS.disk.FAT1.set(pos, Null_Pointer);
                diskSyS.disk.FAT2.set(pos, Null_Pointer);
                return pos;
            }
            pos = diskSyS.disk.FAT1.get(pos);
        }

        return 114;
    }


    /**
     * 释放block空间: 设置为空
     *
     * @param blockNum
     */
    public static void set1BlockFree(int blockNum) {

        //FIXME
    }


    /**
     * 字节数组设置block内容+TXT位置写入
     *
     * @param bytes    字节数组内容
     * @param blockNum 块号
     */
    public static void setBytes21Block(byte[] bytes, int blockNum) {
        diskSyS.disk.BLOCKS.set(blockNum, new block(bytes));
        write1Str2TXT(byte2Str(bytes), WORKSHOP_PATH + DISK_FILE, blockNum);
    }

    public static void setStr21Block(String str, int blockNum) {
        diskSyS.disk.BLOCKS.set(blockNum, new block(str2Byte(str)));
        write1Str2TXT(str, WORKSHOP_PATH + DISK_FILE, blockNum);
    }

    /**
     * 磁盘的读取一个block
     *
     * @param path 磁盘TXT映射文件路径
     * @param pos  行号(位置)
     * @return String化的内容对象(还是byte)
     */
    public String read1Block(String path, Integer pos) {

        File diskFile = new File(path);
        String res = "";
        if (!diskFile.exists()) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                log.error("创建磁盘映射文件失败, 错误日志: {}", e.getMessage());
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(WORKSHOP_PATH + DISK_FILE), StandardCharsets.UTF_8))) {
            //跳过前面的pos-1行, 读取pos行的内容到s
            for (int i = 0; i < pos - 1; i++) {
                br.readLine();
            }
            res = br.readLine();

        } catch (Exception e) {
            log.error("读取磁盘映射文件{}失败, 错误日志: {}", path, e.getMessage());
        }

        return res;
    }

}
