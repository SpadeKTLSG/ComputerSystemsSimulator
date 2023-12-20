package css.out.file.handle;

import css.out.file.entity.file;

public abstract class HandleFile {

    /**
     * 获得文件内容String类型的长度, 单位:字节
     *
     * @param file 文件
     * @return 文件内容String类型的长度
     */
    public static Integer getFileContextLength(file file) {
        return file.getContent().length();
    }

    /**
     * 根据内容设置文件内容String类型的长度, 单位:字节
     *
     * @param content 文件内容
     * @return 文件内容String类型的长度
     */
    public static Integer setFileContextLength(String content) {
        return content.length();
    }
}
