package css.out.file.entity;

import css.out.file.enums.ROOT_PATH;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.handleB.HandleBlock.getFreeBlock;
import static css.out.file.handleB.HandlePath.getROOT_DIRPath;
import static css.out.file.utils.ByteUtil.byteMerger;
import static css.out.file.entiset.GF.DIR_NAME_DEFAULT;

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
        this.fcb = new FCB(getROOT_DIRPath(ROOT_PATH.tmp) + ':' + DIR_NAME_DEFAULT, getFreeBlock(), DIR);
        //TODO 标记磁盘块为已使用
    }

    /**
     * dir转换为Bytes, 直接FCB转Bytes + 内容转Bytes(空)
     *
     * @return Bytes
     */
    public byte[] toBytes() {
        return byteMerger(fcb.toBytes(), content.getBytes());
    }

    public void fromBytes(byte[] bytes) {
        this.fcb = this.fcb.fromBytes(bytes);
    }

}
