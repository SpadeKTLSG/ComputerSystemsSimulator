package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.utils.GlobalField.DIR_NAME_DEFAULT;
import static css.out.file.utils.HandleBlock.GetFreeBlock;
import static css.out.file.utils.HandlePath.getROOT_DIRPath;

@Slf4j
@Data
public class dir {

    /**
     * 文件控制块
     */
    public FCB fcb;


    /**
     * 文件内容
     * <p>锁定为"" - 维持通用性</p>
     */
    private final String content = "";

    /**
     * 默认取消打印空内容
     *
     * @return 文件夹信息
     */
    @Override
    public String toString() {
        return "dir{" +
                fcb +
                '}';
    }

    public dir(FCB fcb) {
        this.fcb = fcb;
    }

    /**
     * 文件夹完全指定构造
     *
     * @param pathName   目录名+文件夹名
     * @param startBlock 起始盘块号
     */
    public dir(String pathName, int startBlock) {
        this.fcb = new FCB(pathName, startBlock, DIR);
        //TODO 标记磁盘块为已使用
    }


    /**
     * 文件夹临时生成
     * <p>默认走/tmp目录</p>
     */
    public dir() {
        this.fcb = new FCB(getROOT_DIRPath(ROOT_PATH.tmp) + ':' + DIR_NAME_DEFAULT, GetFreeBlock(), DIR);
        //TODO 标记磁盘块为已使用
    }

    //TODO FILE -> byte[]
}
