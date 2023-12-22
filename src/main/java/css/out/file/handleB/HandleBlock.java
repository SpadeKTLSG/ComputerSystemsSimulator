package css.out.file.handleB;

import css.out.file.entity.block;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.entiset.GF.*;
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


    //! 2.TXT操作

    /**
     * Str msg直接狠狠注入TXT对应行
     * <p>这样我们就真的回不到过去了, 前辈!</p>
     * <p>需要上级调用控制写入不越界</p>
     *
     * @param msg  要写入的字符串
     * @param path 目标TXT文件路径
     * @param pos  位置
     */
    public static void write1Str2TXT(String msg, String path, Integer pos) {
        StringBuilder sb = new StringBuilder();            //全部读取保存到StringBuffer
        Integer pos_temp = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (pos_temp.equals(pos)) {
                    //FIXME
                    // 在这里要把msg处理长度: 默认是不会超过一行的最大长度, 因为上级调用会进行判断;
                    //这里先完成补长度操作: 补位到BLOCK_SIZE
                    if (msg.length() < BLOCK_SIZE) {
                        msg = msg + " ".repeat(BLOCK_SIZE - msg.length());
                    }
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
     * 磁盘的读取一个block
     *
     * @param path 磁盘TXT映射文件路径
     * @param pos  行号(位置)
     * @return String化的内容对象(还是byte)
     */
    public String read1BlockTXT(String path, Integer pos) {

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

    //! 3.混合操作

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

    /**
     * 字符串设置block内容+TXT位置写入
     *
     * @param str      字符串内容
     * @param blockNum 块号
     */
    public static void setStr21Block(String str, int blockNum) {
        diskSyS.disk.BLOCKS.set(blockNum, new block(str2Byte(str)));
        write1Str2TXT(str, WORKSHOP_PATH + DISK_FILE, blockNum);
    }

}
