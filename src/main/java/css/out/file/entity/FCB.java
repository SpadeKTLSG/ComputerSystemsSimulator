package css.out.file.entity;

import css.out.file.enums.FCB_FIELD;
import css.out.file.enums.FileDirTYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.handle.HandlePath.*;
import static css.out.file.utils.ByteUtil.Int2Byte;
import static css.out.file.utils.GlobalField.*;

/**
 * FCB 文件控制块
 * 8B绑定文件/目录HEAD, 用于描述文件的基本信息
 */
@Slf4j
@Data
@AllArgsConstructor
public class FCB {

    /**
     * !目录名(/分割)+ (:) +文件名(不包含:)
     * <p>从/(根目录)唯一查找到该文件</p>
     * <p>需要被加载到PathManager中</p>
     * <p>e.g.</p>
     * <p>/home/114514:     小本本.txt</p>
     * <p>/home/114514:     上课文件夹</p>
     * <p>/home:            114514</p>
     */
    public String pathName; //TODO 通过工具类来获取路径

    /**
     * 起始盘块号
     * <p>3 <= startBlock <= 127</p>
     */
    public Integer startBlock;

    /**
     * 扩展名(目录为空)
     *
     * <p>需要被加载到扩展名映射表中</p>
     */
    public String extendName; //TODO 通过工具类来获取

    /**
     * 文件目录标识
     * <p> -> FILE / DIR</p>
     * <p>需要被加载到标识映射表中</p>
     */
    public FileDirTYPE typeFlag;

    /**
     * 文件长度(目录为空)
     * <p>单位:字节</p>
     * <p>用工具类计算占用的块数</p>
     */
    public Integer fileLength;


    /**
     * 指定目录和磁盘块快速新建文件/文件夹构造
     *
     * @param pathName   目录名+文件名
     * @param startBlock 起始盘块号
     * @param typeFlag   文件or目录标识
     */
    public FCB(String pathName, int startBlock, FileDirTYPE typeFlag) {
        if (typeFlag == DIR) { //目录

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = DIR_EXTEND;
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + DIR_LENGTH_DEFAULT;

        } else if (typeFlag == FILE) { //文件

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = FILE_EXTEND_DEFAULT;
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + FILE_LENGTH_DEFAULT;

        } else { //出错
            //TODO 提示用户异常信息
            log.error("FCB构造失败, 传递flag: {} 错误", typeFlag);
        }

    }

    /**
     * 根据是DIR还是FILE来构造FCB的toString方法
     *
     * @return FCB信息
     */
    @Override
    public String toString() {
        if (typeFlag == DIR) {
            return "FCB{" +
                    "pathName='" + pathName + '\'' +
                    ", startBlock=" + startBlock +
                    ", typeFlag=" + typeFlag +
                    ", fileLength=" + fileLength +
                    '}';
        } else {
            return "FCB{" +
                    "pathName='" + pathName + '\'' +
                    ", startBlock=" + startBlock +
                    ", extendName='" + extendName + '\'' +
                    ", typeFlag=" + typeFlag +
                    ", fileLength=" + fileLength +
                    '}';
        }
    }


    /**
     * ?FCB转换为Bytes
     *
     * @return Bytes
     */
    public byte[] toBytes() {
        byte[] bytes = new byte[FCB_BYTE_LENGTH];
        int index = 0;

        for (FCB_FIELD field : FCB_FIELD.values()) {

            int length = FCB_LENGTH.get(field.getName());

            byte[] value = switch (field) { //不能使用任何简单的toString, 需要自己转换为对应映射表
                case PATH_NAME -> Int2Byte(bindPathManager(this));
                case START_BLOCK -> Int2Byte(this.startBlock);
                case EXTEND_NAME -> Int2Byte(selectExtendManager(this));
                case TYPE_FLAG -> Int2Byte(FileorDir2Int(this));
                case FILE_LENGTH -> Int2Byte(this.fileLength);
            };

            byte[] valueBytes = toFixedLengthBytes(value, length);
            System.arraycopy(valueBytes, 0, bytes, index, length);//arraycopy(源数组, 源数组起始位置, 目标数组, 目标数组起始位置, 复制长度)
            // 更新index的值，加上复制的长度
            index += length;
        }

        return bytes;
    }

    /**
     * ?将一个指定的字节数组对象转换为固定对应长度的字节数组
     * <p>删除了压缩逻辑</p>
     *
     * @param bytes     指定的字节数组
     * @param len 指定的数组长度
     * @return 转换后的字节数组
     */
    private byte[] toFixedLengthBytes(byte[] bytes, int len) {

        if (bytes.length < len) { // 如果长度小于len，就在右边填充空格
            byte[] padded = new byte[len];
            System.arraycopy(bytes, 0, padded, 0, bytes.length);
            for (int i = bytes.length; i < len; i++) {
                padded[i] = ' ';
            }
            return padded;
        } else if (bytes.length > len) { // 如果compressed的长度大于len，就截取左边的len个字节
            log.warn("有对象被截断");
            byte[] truncated = new byte[len];
            System.arraycopy(bytes, 0, truncated, 0, len);
            return truncated;
        }
        // 如果compressed的长度等于len，就直接返回
        else {
            return bytes;
        }
    }


    /**
     * Bytes转换为FCB
     *
     * @param bytes Bytes
     * @return FCB
     */
    public FCB fromBytes(byte[] bytes) {

        return this;
    }

    /**
     * 从Bytes中设置对应的值
     *
     * @param key        FCB中的key
     * @param valueBytes 对应的Bytes
     */
    private void setBytesForType(String key, byte[] valueBytes) {

    }


}

