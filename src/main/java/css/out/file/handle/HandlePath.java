package css.out.file.handle;

import css.out.file.entity.FCB;
import css.out.file.enums.ROOT_PATH;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.enums.FileDirTYPE.FILE;


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


    /**
     * FCB的PathName绑定路径管理器
     * <p>这样硬盘只需要存储对应的键即可</p>
     *
     * @param fcb FCB
     * @return 绑定的键
     */
    public static Integer bindPathManager(FCB fcb) {

        //在PathManager找一个空白位置插入索引项
        List<Integer> keys = fileSyS.pathManager.entrySet().stream() //将Map转换为Stream，过滤出值等于目标值的键值对，映射为键，收集为集合
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        log.info("当前path中的空白位置: {}", keys.size());
        Integer A = keys.get(0);
        fileSyS.pathManager.put(A, fcb.getPathName());

        return A;
    }

    /**
     * FCB的ExtendName绑定扩展名管理器
     * <p>这样硬盘只需要存储对应的键即可</p>
     *
     * @param fcb FCB
     * @return 绑定的键
     */
    public static Integer bindExtendManager(FCB fcb) {


        List<Integer> keys = fileSyS.extendManager.entrySet().stream()       //在extendManager找一个空白位置插入索引项
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .toList();

        log.info("当前extend中的空白位置: {}", keys.size());
        Integer A = keys.get(0);
        fileSyS.extendManager.put(A, fcb.getExtendName());

        return A;
    }

    /**
     * 扩展名管理器中找FCB的ExtendName
     *
     * @param fcb FCB
     * @return 扩展名管理器中的键
     */
    public static Integer selectExtendManager(FCB fcb) {
        //在extendManager找对应的键, 找不到报错
        List<Integer> keys = fileSyS.extendManager.entrySet().stream()
                .filter(entry -> entry.getValue().equals(fcb.getExtendName()))
                .map(Map.Entry::getKey)
                .toList();

        return keys.get(0);
    }

    /**
     * FCB的TypeFlag标识转Int
     *
     * @param fcb FCB
     * @return 0: 文件, 1: 文件夹
     */
    public static Integer FileorDir2Int(FCB fcb) {

        return fcb.getTypeFlag().equals(FILE) ? 0 : 1;
    }

}
