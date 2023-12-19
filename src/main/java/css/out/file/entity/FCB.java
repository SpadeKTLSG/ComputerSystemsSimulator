package css.out.file.entity;

import css.out.file.enums.FileDirTYPE;
import css.out.file.utils.GzipUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static css.out.file.enums.FileDirTYPE.DIR;
import static css.out.file.enums.FileDirTYPE.FILE;
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
     * !目录名(/分割)+ (:) +文件名(不包含:)
     * <p>从/(根目录)唯一查找到该文件</p>
     * <p>e.g.</p>
     * <p>/home/114514:     小本本.txt</p>
     * <p>/home/114514:     上课文件夹</p>
     * <p>/home:            114514</p>
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
     * <p> -> FILE / DIR</p>
     */
    public FileDirTYPE typeFlag;

    /**
     * 文件长度(目录为空)
     * <p>单位:字节</p>
     * <p>用工具类计算占用的块数</p>
     */
    public Integer fileLength;


    /**
     * 指定目录和磁盘块快速新建文件/文件夹构造
     *
     * @param pathName   目录名+文件名
     * @param startBlock 起始盘块号
     * @param typeFlag   文件or目录标识
     */
    public FCB(String pathName, int startBlock, FileDirTYPE typeFlag) {
        if (typeFlag == DIR) { //目录

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = DIR_EXTEND;
            this.typeFlag = DIR;
            this.fileLength = FCB_BYTE_LENGTH + DIR_LENGTH_DEFAULT;

        } else if (typeFlag == FILE) { //文件

            this.pathName = pathName;
            this.startBlock = startBlock;
            //autofill
            this.extendName = FILE_EXTEND_DEFAULT;
            this.typeFlag = FILE;
            this.fileLength = FCB_BYTE_LENGTH + FILE_LENGTH_DEFAULT;

        } else { //出错
            //TODO 提示用户异常信息
            log.error("FCB构造失败, 传递flag: {} 错误", typeFlag);
        }

    }

    /**
     * 根据是DIR还是FILE来构造FCB的toString方法
     *
     * @return FCB信息
     */
    @Override
    public String toString() {
        if (typeFlag == DIR) {
            return "FCB{" +
                    "pathName='" + pathName + '\'' +
                    ", startBlock=" + startBlock +
                    ", typeFlag=" + typeFlag +
                    ", fileLength=" + fileLength +
                    '}';
        } else {
            return "FCB{" +
                    "pathName='" + pathName + '\'' +
                    ", startBlock=" + startBlock +
                    ", extendName='" + extendName + '\'' +
                    ", typeFlag=" + typeFlag +
                    ", fileLength=" + fileLength +
                    '}';
        }
    }


    /**
     * 将一个String对象压缩并转换为固定长度的字节数组
     *
     * @param s   要转换的String对象
     * @param len 指定的长度
     * @return 转换后的字节数组
     */
    private byte[] toFixedLengthBytes(String s, int len) {
        try {
            // 调用Gziputil的zip方法，对字符串进行压缩，返回压缩后的字节数组
            byte[] compressed = GzipUtil.zip(s);
            // 如果compressed的长度小于len，就在右边填充空格
            if (compressed.length < len) {
                //通知: 有对象被填充
                log.info("有对象被填充,{}", s);
                byte[] padded = new byte[len];
                System.arraycopy(compressed, 0, padded, 0, compressed.length);
                for (int i = compressed.length; i < len; i++) {
                    padded[i] = ' ';
                }
                return padded;
            }
            // 如果compressed的长度大于len，就截取左边的len个字节
            else if (compressed.length > len) {
                //报警: 有对象被截断
                log.warn("有对象被截断,{}", s);
                byte[] truncated = new byte[len];
                System.arraycopy(compressed, 0, truncated, 0, len);
                return truncated;
            }
            // 如果compressed的长度等于len，就直接返回
            else {
                return compressed;
            }
        } catch (IOException e) {
            // 如果发生异常，抛出异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 将一个压缩后的字节数组解压并还原为原始的String对象
     *
     * @param bytes 要解压的字节数组
     * @return 还原后的String对象
     */
    public String unzip(byte[] bytes) {
        try {
            // 调用Gziputil的unzip方法，对压缩后的字节数组进行解压缩，返回还原后的字符串
            return GzipUtil.unzip(bytes);
        } catch (IOException e) {
            // 如果发生异常，抛出异常
            throw new RuntimeException(e);
        }
    }

    //my

    /**
     * !FCB转换为Bytes
     *
     * @return Bytes
     */
    public byte[] toBytes() {

        byte[] bytes = new byte[FCB_BYTE_LENGTH]; //初始byte数组 = 8B大小
        int index = 0;

        for (String key : FCB_LENGTH.keySet()) { //遍历FCB_BYTE_LENGTH_MAP
            System.out.println(key);
            byte[] valueBytes = getBytesForType(key);
            if (valueBytes != null) {
                System.arraycopy(valueBytes, 0, bytes, index, FCB_LENGTH.get(key)); //arraycopy(源数组, 源数组起始位置, 目标数组, 目标数组起始位置, 复制长度)
            }
            //这里bug, index没有加上对应的值而是每次都是+1
            System.out.println(FCB_LENGTH.get(key));

            index += FCB_LENGTH.get(key);
        }

        return bytes;
    }

    /**
     * 从FCB中获取对应的Bytes
     *
     * @param key FCB中的key
     * @return 对应的Bytes
     */
/*    private byte[] getBytesForType(String key) {
        return switch (key) {
            case "pathName" -> pathName.getBytes();
            case "extendName" -> extendName.getBytes();
            case "typeFlag" -> typeFlag.toString().getBytes();
            case "startBlock" -> startBlock.toString().getBytes();
            case "fileLength" -> fileLength.toString().getBytes();
            default -> null;
        };
    }*/


    /**
     * 从FCB中获取对应的Bytes
     *
     * @param key FCB中的key
     * @return 对应的Bytes
     */
    public byte[] getBytesForType(String key) {
        return switch (key) {
            case "pathName" -> toFixedLengthBytes(pathName, 3);
            case "extendName" -> toFixedLengthBytes(extendName, 2);
            case "typeFlag" -> toFixedLengthBytes(typeFlag.toString(), 1);
            case "startBlock" -> toFixedLengthBytes(startBlock.toString(), 1);
            case "fileLength" -> toFixedLengthBytes(fileLength.toString(), 1);
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
        for (String key : FCB_LENGTH.keySet()) {
            byte[] valueBytes = new byte[FCB_LENGTH.get(key)];
            System.arraycopy(bytes, index, valueBytes, 0, FCB_LENGTH.get(key));
            index += FCB_LENGTH.get(key);
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
            case "typeFlag" -> typeFlag = FileDirTYPE.valueOf(new String(valueBytes));
            case "startBlock" -> startBlock = Integer.parseInt(new String(valueBytes));
            case "fileLength" -> fileLength = Integer.parseInt(new String(valueBytes));
            default -> {
            }
        }
    }


}

