package css.out.file.handleS;

import css.out.file.entity.block;
import css.out.file.entity.dir;
import css.out.file.entity.file;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static css.out.file.FileApp.diskSyS;
import static css.out.file.api.CommonApiList.alertUser;
import static css.out.file.entiset.GF.*;
import static css.out.file.entity.disk.initialDisk;
import static css.out.file.handleB.HandleBlock.setBytes21Block;
import static css.out.file.handleB.HandleDISK.*;
import static css.out.file.handleB.HandleFile.*;
import static css.out.file.handleB.HandleTXT.readAllTXT2Str;
import static css.out.file.handleB.HandleTXT.writeAllDISK2TXT;
import static css.out.file.utils.ByteUtil.str2Byte;

/**
 * 0级 磁盘系统管理工具类
 */
@Slf4j
public abstract class HandleDS {

    //! 1. 磁盘系统/

    /**
     * ?正常模式
     * <p>从磁盘映射文件完全加载磁盘系统</p>
     */
    public static void normalRebootDisk() {
        putStr2Disk(readAllTXT2Str(WORKSHOP_PATH + DISK_FILE));
        log.debug("磁盘模块重读完成");
    }


    /**
     * ?覆盖模式
     * <p>直接用当前JAVA磁盘对象覆盖磁盘映射文件</p>
     */
    public static void coverRebootDisk() {
        //手动把当前的FAT覆盖磁盘
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
        //写入磁盘
        writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        putStr2Disk(readAllTXT2Str(WORKSHOP_PATH + DISK_FILE));
        log.debug("磁盘模块覆盖完成");
    }


    /**
     * ?格式化模式
     * <p>重新格式化磁盘, 会清空磁盘中的所有数据</p>
     */
    public static void cleanRebootDisk() {
        //获取新磁盘对象
        diskSyS.disk = initialDisk();
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象
        //写入磁盘
        writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);
        putStr2Disk(readAllTXT2Str(WORKSHOP_PATH + DISK_FILE));
        log.debug("{}格式化完成!", diskSyS.disk.name);
    }


    //! 2. 磁盘/


    /**
     * 获取默认格式化的空BLOCKS
     *
     * @return 默认BLOCKS
     */
    public static List<block> initialBLOCKS() {

        List<block> BLOCKS = new ArrayList<>(DISK_SIZE);
        for (int i = 0; i < DISK_SIZE; i++)
            BLOCKS.add(new block());


        return BLOCKS;
    }


    /**
     * StrTXT内容赋值BLOCKJava对象
     *
     * @param great_str 磁盘映射文件长字符串
     */
    public static void putStr2Disk(String great_str) {

        String[] str = great_str.split("\n");

        for (int i = 0; i < DISK_SIZE; i++) {

            if (i == FAT1_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                diskSyS.disk.FAT1 = Bytes2FAT(bytes_temp);
                mountFAT2BLOCKS(diskSyS.disk.BLOCKS, bytes_temp, 1);
                continue;
            }

            if (i == FAT2_DIR) {
                byte[] bytes_temp = str2Byte(str[i]);
                diskSyS.disk.FAT2 = Bytes2FAT(bytes_temp);
                mountFAT2BLOCKS(diskSyS.disk.BLOCKS, bytes_temp, 2);
                continue;
            }

            setStr21Block_TXT(str[i], i); //或者是setBytes21Block
        }

        //writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE); //二次写入磁盘保证一致性
        log.debug("{}初始化完成!", diskSyS.disk.name);
    }


    /**
     * 将文件对象写入磁盘模块
     *
     * @param A 文件/文件夹对象
     */
    public static void writeContext(Object A) {


        //0. 合法性校验
        //0.1 长度校验
        if (A instanceof file file_temp) {
            if (getFileLength(file_temp) > BLOCK_SIZE) {
                log.warn("文件{}过大, 无法保存", file_temp.fcb.getPathName());
                alertUser("文件过大, 无法保存");
                return;
            }
        }


        //1. 占用盘块资源 in FAT
        Integer inputPos = set1BlockUse();
        log.debug("占用了盘块: {}", inputPos);

        System.out.println(getFATOrder());//康康FAT占用顺序

        if (inputPos == -1) {
            log.warn("系统磁盘爆炸咯!");
            alertUser("系统磁盘被撑爆了, Behave yourself!");
            return;
        }


        //2. 文件属性处理 + 2.5 写入BLOCKS

        if (A instanceof file file_temp) {
            log.debug("正在保存文件{}", ((file) A).fcb.getPathName());
            //?属性拷贝: 对于DTO文件XXX.txt, 创建path和文件名(扩展名)和文件类型都已经赋值好. (仿照Linux交互)
            file_temp.fcb.setStartBlock(inputPos); //设置文件起始块
            file_temp.fcb.setFileLength(getFileLength(file_temp)); //设置文件长度
            setBytes21Block(file2Bytes(file_temp), inputPos); //写入磁盘

        } else if (A instanceof dir dir_temp) {
            log.debug("正在保存文件夹{}", ((dir) A).fcb.getPathName());
            dir_temp.fcb.setStartBlock(inputPos); //设置文件起始块
            setBytes21Block(dir2Bytes(dir_temp), inputPos); //写入磁盘

        } else {
            log.warn("不是文件也不是文件夹, 你是什么东西?{}", A);
            //从磁盘刷新系统, 还原被占用的FAT空间
            normalRebootDisk();
            throw new RuntimeException("被投喂了奇怪的东西, 我当场趋势: " + A);
        }

        //3. FAT写入BLOCKS
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT1), 1); //挂载FAT1字节对象
        mountFAT2BLOCKS(diskSyS.disk.BLOCKS, FAT2Bytes(diskSyS.disk.FAT2), 2); //挂载FAT2字节对象

        //4. BLOCKS全量覆写TXT
        writeAllDISK2TXT(diskSyS.disk.BLOCKS, WORKSHOP_PATH + DISK_FILE);


    }


}
