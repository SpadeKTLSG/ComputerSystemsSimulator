package css.out.file.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.utils.GlobalField.*;

/**
 * FCB 文件控制块
 * 8B绑定文件/目录HEAD, 用于描述文件的基本信息
 */
@Slf4j
@Data
@AllArgsConstructor
public class FCB {

    /**
     * 目录名(/分割)+ (:) +文件名(不包含:)
     * <p>从/(根目录)唯一查找到该文件</p>
     * <p>e.g.</p>
     * <p>/home/114514 小本本.txt</p>
     * <p>/home/114514 上课文件夹</p>
     * <p>/home 114514</p>
     */
    public String pathName; //TODO 通过工具类来获取路径

    /**
     * 起始盘块号
     * <p>3 <= startBlock <= 127</p>
     */
    public Integer startBlock;

    /**
     * 扩展名(目录为空)
     */
    public String extendName; //TODO 通过工具类来获取

    /**
     * 文件目录标识
     * <p> -> FILE_SIGNAL:0 / DIR_SIGNAL:1</p>
     */
    public Integer typeFlag;

    /**
     * 文件长度(目录为空)
     * <p>单位:字节</p>
     */
    public Integer fileLength;


    /**
     * 指定目录和磁盘块快速新建文件/文件夹构造
     *
     * @param pathName   目录名+文件名
     * @param startBlock 起始盘块号
     * @param typeFlag   文件or目录标识
     */
    public FCB(String pathName, int startBlock, int typeFlag) {
        if (typeFlag == DIR_SIGNAL) { //目录

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = EMPTY_DIR_TYPE;
            this.typeFlag = DIR_SIGNAL;
            this.fileLength = FCB_BYTE_LENGTH + DEFAULT_DIR_LENGTH;

        } else if (typeFlag == FILE_SIGNAL) { //文件

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = EMPTY_FILE_TYPE;
            this.typeFlag = FILE_SIGNAL;
            this.fileLength = FCB_BYTE_LENGTH + DEFAULT_FILE_LENGTH;

        } else { //出错
            //TODO 提示用户异常信息
            log.error("FCB构造失败, 传递flag: {} 错误", typeFlag);
        }

    }


    /**
     * FCB转换为Bytes
     *
     * @return Bytes
     */
    public byte[] toBytes() {

        byte[] bytes = new byte[FCB_BYTE_LENGTH]; //初始byte数组
        int index = 0;

        for (String key : FCB_BYTE_LENGTH_MAP.keySet()) { //遍历FCB_BYTE_LENGTH_MAP
            byte[] valueBytes = getBytesForType(key);
            if (valueBytes != null) {
                System.arraycopy(valueBytes, 0, bytes, index, FCB_BYTE_LENGTH_MAP.get(key)); //arraycopy(源数组, 源数组起始位置, 目标数组, 目标数组起始位置, 复制长度)
            }
            index += FCB_BYTE_LENGTH_MAP.get(key);
        }

        return bytes;
    }

    /**
     * 从FCB中获取对应的Bytes
     *
     * @param key FCB中的key
     * @return 对应的Bytes
     */
    private byte[] getBytesForType(String key) {
        return switch (key) {
            case "pathName" -> pathName.getBytes();
            case "extendName" -> extendName.getBytes();
            case "typeFlag" -> typeFlag.toString().getBytes();
            case "startBlock" -> startBlock.toString().getBytes();
            case "fileLength" -> fileLength.toString().getBytes();
            default -> null;
        };
    }

    /**
     * Bytes转换为FCB
     *
     * @param bytes Bytes
     * @return FCB
     */
    public FCB fromBytes(byte[] bytes) {

        int index = 0;
        for (String key : FCB_BYTE_LENGTH_MAP.keySet()) {
            byte[] valueBytes = new byte[FCB_BYTE_LENGTH_MAP.get(key)];
            System.arraycopy(bytes, index, valueBytes, 0, FCB_BYTE_LENGTH_MAP.get(key));
            index += FCB_BYTE_LENGTH_MAP.get(key);
            setBytesForType(key, valueBytes);
        }

        return this;
    }

    /**
     * 从Bytes中设置对应的值
     *
     * @param key        FCB中的key
     * @param valueBytes 对应的Bytes
     */
    private void setBytesForType(String key, byte[] valueBytes) {
        switch (key) {
            case "pathName" -> pathName = new String(valueBytes);
            case "extendName" -> extendName = new String(valueBytes);
            case "typeFlag" -> typeFlag = Integer.parseInt(new String(valueBytes));
            case "startBlock" -> startBlock = Integer.parseInt(new String(valueBytes));
            case "fileLength" -> fileLength = Integer.parseInt(new String(valueBytes));
            default -> {
            }
        }
    }


}

