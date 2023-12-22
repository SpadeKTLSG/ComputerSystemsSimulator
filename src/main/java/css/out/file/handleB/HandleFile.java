package css.out.file.handleB;

import css.out.file.entity.FCB;
import css.out.file.entity.file;
import css.out.file.enums.FileDirTYPE;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;

/**
 * II级 文件/文件夹工具类
 */
@Slf4j
public abstract class HandleFile {

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
        return file.getContent().length();
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



}
