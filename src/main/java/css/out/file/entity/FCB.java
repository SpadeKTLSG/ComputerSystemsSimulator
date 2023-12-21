package css.out.file.entity;

import css.out.file.enums.FCB_FIELD;
import css.out.file.enums.FileDirTYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.api.CommonApiList.alertUser;
import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.handle.HandleFile.fromFixedLengthBytes;
import static css.out.file.handle.HandleFile.toFixedLengthBytes;
import static css.out.file.handle.HandlePath.*;
import static css.out.file.utils.ByteUtil.Int2Byte;
import static css.out.file.entity.GF.*;

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
    public String pathName;

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
    public String extendName;

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
            this.extendName = DIR_EXTEND.get(0);
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + DIR_LENGTH_DEFAULT;

        } else if (typeFlag == FILE) { //文件

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = FILE_EXTEND.get(0);
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + FILE_LENGTH_DEFAULT;

        } else { //出错
            alertUser("FCB构造失败, 传递flag: " + typeFlag + " 错误");
            log.error("FCB构造失败, 传递flag: {} 错误", typeFlag);
        }

    }

    /**
     * 指定FCB类型快速构建无挂载的对象(禁止)
     *
     * @param typeFlag 文件or目录标识
     */
    public FCB(FileDirTYPE typeFlag) {
        log.debug("正在构建一个空白文件/文件夹类型FCB");
        if (typeFlag == DIR) { //目录

            //autofill
            this.extendName = DIR_EXTEND.get(0);
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + DIR_LENGTH_DEFAULT;

        } else if (typeFlag == FILE) { //文件

            //autofill
            this.extendName = FILE_EXTEND.get(0);
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + FILE_LENGTH_DEFAULT;

        } else { //出错
            alertUser("FCB构造失败, 传递flag: " + typeFlag + " 错误");
            log.error("FCB构造失败, 传递flag: {} 错误", typeFlag);
        }
    }

    /**
     * 空白FCB构造(禁止)
     */
    public FCB() {
        log.debug("正在构建一个空白FCB");
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
     * FCB转换为Bytes
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
     * Bytes转换为FCB
     * <p>通过new FCB.调用</p>
     */
    public FCB fromBytes(byte[] bytes) {

        int index = 0;
        for (FCB_FIELD field : FCB_FIELD.values()) {//按照相同的逻辑, 从bytes中截取对应的字节, 然后转换为对应的类型对象

            int length = FCB_LENGTH.get(field.getName());

            byte[] valueBytes = new byte[length];
            System.arraycopy(bytes, index, valueBytes, 0, length);//arraycopy(源数组, 源数组起始位置, 目标数组, 目标数组起始位置, 复制长度)


            switch (field) {
                case PATH_NAME -> this.pathName = fromPathManager(fromFixedLengthBytes(valueBytes, length));
                case START_BLOCK -> this.startBlock = fromFixedLengthBytes(valueBytes, length);
                case EXTEND_NAME -> this.extendName = fromExtendManager(fromFixedLengthBytes(valueBytes, length));
                case TYPE_FLAG -> this.typeFlag = Int2FileorDir(fromFixedLengthBytes(valueBytes, length));
                case FILE_LENGTH -> this.fileLength = fromFixedLengthBytes(valueBytes, length);
            }
            index += length;
        }
        return this;
    }



}

