package css.out.file.api;

import css.out.file.entity.file;
import css.out.file.enums.FileDirTYPE;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能api
 */
@Slf4j
public class FunctionApiList {

    //! 文件 - 一次整体交互

    //大致流程

    //? 前端 ->  用户点击前端界面, 调用文件系统功能, 传递前端路径参数(文件名+扩展名+路径+文件/文件夹类型 DTO(FCB))

    //? 文件系统 -> 鉴权, 找到文件在文件系统中Java对象中的位置, 判断无问题后开始事务操作CRUD;
    //?            同时通知进程创建一个文件处理进程, 传递虚拟DTO文件/文件夹(目录)对象到工作文件夹

    //? 进程系统 -> 拿到文件系统的文件路径, 读取文件/文件夹, 获得内容(如果是脚本可执行文件, 需要调用相关功能)
    //?             同时通知内存占用内存空间, 而后启动进程


    //? || 进程处理完了 || -> 通知前端刷新页面, 一次处理流程完成

    public static void handleCommon(String path, String allName, FileDirTYPE type) {
        //? 通用处理
        //? 1. 鉴权
        //? 2. 找到文件在文件系统中Java对象中的位置
        //? 3. 判断无问题后开始事务操作CRUD
        //? 4. 同时通知进程创建一个文件处理进程, 传递虚拟DTO文件/文件夹(目录)对象到工作文件夹
    }

    public static Object getObjectfrFront(String path, String allName, FileDirTYPE type) {
        //? 从前端获取对象
        return null;
    }

    /**
     * @param path
     * @param allName
     * @param type
     */
    public static void notifyProcessSyS(String path, String allName, FileDirTYPE type) {
        log.info("正在通知进程系统创建进程...");
        //封装对象
        if (type.equals(FileDirTYPE.FILE)) {
            //? 文件
            file temp_file = new file();
            //? 通知进程系统创建进程, 传递文件路径
        } else {
            //? 文件夹
            //? 通知进程系统创建进程, 传递文件夹路径
        }
    }

}
