package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.utils.GlobalField.DEFAULT_FILE_NAME;
import static css.out.file.utils.GlobalField.FILE_SIGNAL;
import static css.out.file.utils.HandleBlock.GetFreeBlock;
import static css.out.file.utils.HandlePath.GetDefaultPath;

@Slf4j
@Data
public class file {

    /**
     * 文件控制块
     */
    public FCB fcb;

    /**
     * 文件内容
     */
    public String content;

    /**
     * 文件完全指定构造
     *
     * @param pathName   目录名+文件名
     * @param startBlock 起始盘块号
     * @param content    文件内容
     */
    public file(String pathName, int startBlock, String content) {
        this.fcb = new FCB(pathName, startBlock, FILE_SIGNAL);
        this.content = content;
        //TODO 标记磁盘块为已使用
    }

    /**
     * 由内容文件临时生成
     *
     * @param content 文件内容
     */
    public file(String content) {
        new file(); //直接调用无参构造
        this.content = content;
    }

    /**
     * 无内容文件临时生成
     * <p>默认走/tmp目录</p>
     */
    public file() {
        this.fcb = new FCB(GetDefaultPath(ROOT_PATH.tmp) + ':' + DEFAULT_FILE_NAME, GetFreeBlock(), FILE_SIGNAL);
        this.content = "";
        //TODO 标记磁盘块为已使用
    }

//TODO FILE -> byte[]
}
