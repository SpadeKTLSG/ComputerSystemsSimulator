package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.utils.ByteUtil.byteMerger;
import static css.out.file.entity.GlobalField.FCB_BYTE_LENGTH;
import static css.out.file.entity.GlobalField.FILE_NAME_DEFAULT;
import static css.out.file.handle.HandleBlock.GetFreeBlock;
import static css.out.file.handle.HandlePath.getROOT_DIRPath;

@Slf4j
@Data
public class file {

    /**
     * 文件控制块
     */
    public FCB fcb;

    /**
     * 文件内容
     */
    public String content;

    @Override
    public String toString() {
        return "file{" +
                fcb +
                ", content='" + content + '\'' +
                '}';
    }

    /**
     * 文件FCB直接构建
     *
     * @param fcb     对应绑定的文件控制块
     * @param content 文件内容
     */
    public file(FCB fcb, String content) {
        this.fcb = fcb;
        this.content = content;
    }

    /**
     * 文件完全指定构造
     *
     * @param pathName   目录名+文件名
     * @param startBlock 起始盘块号
     * @param content    文件内容
     */
    public file(String pathName, int startBlock, String content) {
        this.fcb = new FCB(pathName, startBlock, FILE);
        this.content = content;
        //TODO 标记磁盘块为已使用
    }

    /**
     * 由内容文件临时生成
     *
     * @param content 文件内容
     */
    public file(String content) {
        this.fcb = new FCB(getROOT_DIRPath(ROOT_PATH.tmp) + ':' + FILE_NAME_DEFAULT, GetFreeBlock(), FILE);
        this.content = content;
        //TODO 生成其对应的文件大小
    }

    /**
     * 无内容文件临时生成
     * <p>默认走/tmp目录</p>
     */
    public file() {
        log.warn("无内容文件临时生成");
        this.fcb = new FCB(getROOT_DIRPath(ROOT_PATH.tmp) + ':' + FILE_NAME_DEFAULT, GetFreeBlock(), FILE);
        this.content = "";
        //TODO 标记磁盘块为已使用
        //TODO 写入磁盘块
        //TODO
    }

    /**
     * file转换为Bytes, 直接FCB转Bytes + 内容转Bytes
     *
     * @return Bytes
     */
    public byte[] toBytes() {
        return byteMerger(fcb.toBytes(), content.getBytes());
    }

    /**
     * Bytes转换为file
     *
     * @param bytes Bytes
     */
    public void fromBytes(byte[] bytes) {
        //将bytes划分为FCB和内容
        byte[] byte_content = new byte[bytes.length - FCB_BYTE_LENGTH];
        System.arraycopy(bytes, FCB_BYTE_LENGTH, byte_content, 0, bytes.length - FCB_BYTE_LENGTH);
        this.content = new String(byte_content);
        this.fcb = this.fcb.fromBytes(bytes);
    }
}

