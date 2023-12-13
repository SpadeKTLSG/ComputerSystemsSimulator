package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.utils.GlobalField.DEFAULT_DIR_NAME;
import static css.out.file.utils.GlobalField.DIR_SIGNAL;
import static css.out.file.utils.HandleBlock.GetFreeBlock;
import static css.out.file.utils.HandlePath.GetDefaultPath;

@Slf4j
@Data
public class dir {

    /**
     * 文件控制块
     */
    public FCB fcb;

    /**
     * 文件内容
     * <p>锁定为""</p>
     */
    private final String content = "";

    /**
     * 文件夹完全指定构造
     *
     * @param pathName   目录名+文件夹名
     * @param startBlock 起始盘块号
     */
    public dir(String pathName, int startBlock) {
        this.fcb = new FCB(pathName, startBlock, DIR_SIGNAL);
        //TODO 标记磁盘块为已使用
    }


    /**
     * 文件夹临时生成
     * <p>默认走/tmp目录</p>
     */
    public dir() {
        this.fcb = new FCB(GetDefaultPath(ROOT_PATH.tmp) + ':' + DEFAULT_DIR_NAME, GetFreeBlock(), DIR_SIGNAL);
        //TODO 标记磁盘块为已使用
    }

    //TODO FILE -> byte[]
}
