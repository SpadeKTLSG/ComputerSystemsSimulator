package css.out.file.handleB;

import css.out.file.entity.FCB;
import css.out.file.entity.dir;
import css.out.file.entity.file;
import css.out.file.enums.FCB_FIELD;
import css.out.file.enums.FileDirTYPE;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.entiset.GF.FCB_BYTE_LENGTH;
import static css.out.file.entiset.GF.FCB_LENGTH;
import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;
import static css.out.file.handleB.HandlePATH.*;
import static css.out.file.utils.ByteUtil.*;

/**
 * II级 文件/文件夹工具类
 */
@Slf4j
public abstract class HandleFile {

    //! 0. 文件/文件夹


    /**
     * file转换为Bytes, 直接FCB转Bytes + 内容转Bytes
     *
     * @param file file
     * @return Bytes
     */
    public static byte[] file2Bytes(file file) {
        return byteMerger(fcb2Bytes(file.fcb), file.content.getBytes());
    }


    /**
     * Bytes转换为file
     *
     * @param bytes Bytes
     * @return file
     */
    public static file bytes2File(byte[] bytes) {
        file file = new file();

        byte[] byte_content = new byte[bytes.length - FCB_BYTE_LENGTH];        //将bytes划分为FCB和内容
        System.arraycopy(bytes, FCB_BYTE_LENGTH, byte_content, 0, bytes.length - FCB_BYTE_LENGTH);

        file.fcb = bytes2Fcb(bytes);
        file.content = new String(byte_content);
        return file;
    }


    /**
     * dir转换为Bytes
     *
     * @param dir dir
     * @return Bytes
     */
    public static byte[] dir2Bytes(dir dir) {
        return byteMerger(fcb2Bytes(dir.fcb), dir.content.getBytes());
    }


    /**
     * Bytes转换为dir
     *
     * @param bytes Bytes
     * @return dir
     */
    public static dir bytes2Dir(byte[] bytes) {
        dir dir = new dir();
        dir.fcb = bytes2Fcb(bytes);
        return dir;
    }


    //! 1. 文件类型操作


    /**
     * TypeFlag标识转Int
     *
     * @param fcb FCB
     * @return 0: 文件, 1: 文件夹
     */
    public static Integer FileorDir2Int(FCB fcb) {
        return fcb.getTypeFlag().equals(FILE) ? 0 : 1;
    }


    /**
     * Int转FCB的TypeFlag标识
     *
     * @param num 0: 文件, 1: 文件夹
     * @return FCB的TypeFlag标识
     */
    public static FileDirTYPE Int2FileorDir(Integer num) {
        return num.equals(0) ? FILE : DIR;
    }


    //! 2. 文件内容操作


    //! 3. 文件路径操作

    /**
     * Str转Path
     *
     * @param path 路径
     * @return 路径
     */
    public static String str2Path(String path) {
        return '/' + path;
    }


    //! 4. 文件长度操作


    /**
     * 获得文件内容String类型的长度, 单位:字节
     *
     * @param file 文件
     * @return 文件内容String类型的长度
     */
    public static Integer getFileLength(file file) {
        return FCB_BYTE_LENGTH + file.getContent().length();
    }


    /**
     * 根据内容设置文件内容Str长度, 单位:字节
     *
     * @param content 文件内容
     * @return 文件内容String类型的长度
     */
    public static Integer setFileContextLength(String content) {
        return content.length();
    }

    //! 5. FCB操作

    /**
     * FCB转换为Bytes
     *
     * @return Bytes
     */
    public static byte[] fcb2Bytes(FCB fcb) {

        int index = 0;

        byte[] bytes = new byte[FCB_BYTE_LENGTH];

        for (FCB_FIELD field : FCB_FIELD.values()) {

            int length = FCB_LENGTH.get(field.getName());

            Integer temp = switch (field) { //不能使用任何简单的toString, 需要自己转换为对应映射表
                case PATH_NAME -> bindPM(fcb.pathName);
                case START_BLOCK -> fcb.startBlock;
                case EXTEND_NAME -> findKeyiEM(fcb.extendName);
                case TYPE_FLAG -> FileorDir2Int(fcb);
                case FILE_LENGTH -> fcb.fileLength;
            };

            byte[] valueBytes = toFixLenBytes(Int2Byte(temp), length);
            System.arraycopy(valueBytes, 0, bytes, index, length);

            index += length;
        }

        return bytes;
    }


    /**
     * Bytes转换为FCB
     *
     * @param bytes Bytes
     * @return FCB
     */
    public static FCB bytes2Fcb(byte[] bytes) {

        int index = 0;

        FCB fcb = new FCB();

        for (FCB_FIELD field : FCB_FIELD.values()) {

            int length = FCB_LENGTH.get(field.getName());
            byte[] valueBytes = new byte[length];

            System.arraycopy(bytes, index, valueBytes, 0, length);

            Integer temp = byte2Int(valueBytes);

            switch (field) {
                case PATH_NAME -> fcb.pathName = selectPM(temp);
                case START_BLOCK -> fcb.startBlock = temp;
                case EXTEND_NAME -> fcb.extendName = selectEM(temp);
                case TYPE_FLAG -> fcb.typeFlag = Int2FileorDir(temp);
                case FILE_LENGTH -> fcb.fileLength = temp;
            }

            index += length;
        }

        return fcb;
    }


}
