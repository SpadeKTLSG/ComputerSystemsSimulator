package css.out.file.utils;

import css.out.file.entity.ROOT;

/**
 * 获取磁盘块位置工具类
 */
public class GetBlock {

    public static int Find_diskNewFreeBlock() {
        //TODO: 从磁盘块中找到一个空闲的磁盘块位置
        return 114;
    }


    public static int Find_diskNewFreeBlock_of(ROOT path) {
        //TODO: 从磁盘块中找到一个连接在XXX根目录之后的空闲的磁盘块位置
        switch (path) {
            case home -> {
            }
            case app -> {
            }
            case tmp -> {
            }
            case conf -> {
            }
            case mnt -> {
            }
            case bin -> {
            }
            case lib -> {
            }
            case boot -> {
            }
        }
        return 114;
    }
}
