package css.out.file.utils;

import css.out.file.enums.ROOT_PATH;

/**
 * 路径序列相关工具类
 * 获得路径之后用空格分隔文件与目录, 组成pathName
 */
public class HandlePath {

    /**
     * 获得根路径下的默认路径
     *
     * @param path 八大路径之一
     * @return 对应路径的String序列
     */
    public static String GetDefaultPath(ROOT_PATH path) {
        switch (path) {
            case home -> {
                return "/home";
            }
            case app -> {
                return "/app";
            }
            case tmp -> {
                return "/tmp";
            }
            case conf -> {
                return "/conf";
            }
            case mnt -> {
                return "/mnt";
            }
            case bin -> {
                return "/bin";
            }
            case lib -> {
                return "/lib";
            }
            case boot -> {
                return "/boot";
            }
        }
        return "114"; //FIXME
    }
}
