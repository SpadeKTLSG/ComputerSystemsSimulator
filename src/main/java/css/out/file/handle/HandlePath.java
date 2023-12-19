package css.out.file.handle;

import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;


/**
 * 路径序列相关工具类
 * 获得路径之后用空格分隔文件与目录, 组成pathName
 */
@Slf4j
public class HandlePath {

    /**
     * 获得根路径下的默认路径
     *
     * @param path 八大路径之一
     * @return 对应路径的String序列
     */
    public static String getROOT_DIRPath(ROOT_PATH path) {

        return '/' + String.valueOf(path);
    }
}
