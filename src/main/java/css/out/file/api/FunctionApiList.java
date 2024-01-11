package css.out.file.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 功能api
 */
@Slf4j
public class FunctionApiList {

    //! 文件 - 一次整体交互测试模块

    //大致流程

    //? 前端 ->  用户点击前端界面, 调用文件系统功能

    //? 文件系统 -> 鉴权, 找到文件在文件系统中Java对象中的位置, 判断无问题后开始事务操作CRUD;
    //?            同时通知进程创建一个文件处理进程, 传递虚拟DTO文件/文件夹(目录)对象到工作文件夹

    //? 进程系统 -> 拿到文件系统的文件路径, 读取文件/文件夹, 获得内容(如果是脚本可执行文件, 需要调用相关功能)
    //?             同时通知内存占用内存空间, 而后启动进程


    //? || 进程处理完了 || -> 通知前端刷新页面, 一次处理流程完成

    /**
     * 通用前端 - > 文件 -> 进程 处理
     *
     * @param pathName 文件路径全名
     * @param extend   扩展名
     * @param content  文件内容
     */
    public static void handleCommon(String pathName, String extend, String content) { //Controller

        String allName = pathName.split(":")[1] + extend;
        notifyProcessSyS(mkObject(allName, content));        //通知进程创建一个文件处理进程, 传递虚拟DTO文件/文件夹(目录)对象到工作文件夹
    }

    /**
     * 创建文件到缓冲区
     *
     * @param allName 文件名(包含后缀)
     * @param content 文件内容
     * @return 文件路径
     */
    @Transactional
    public static Path mkObject(String allName, String content) {

        File resourcesDirectory = new File("src/main/resources/static/tmp");
        //首先清空与进程模块交互的缓冲区 : 清空tmp文件夹内容
        File[] files = resourcesDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.delete()) {
                    log.warn("删除缓冲区文件{} 失败", file.getName());
                }
            }
        }

        //log.debug("清理缓冲区{} 成功", resourcesDirectory.getName());

        File newFile = new File(resourcesDirectory.getAbsolutePath() + "/" + allName);

        try {
            Files.createFile(newFile.toPath());
            if (!newFile.exists()) {
                log.warn("创建文件{} 失败", newFile.getName());
            }
        } catch (IOException e) {
            log.warn("创建文件{} 失败, errorStack{}", newFile.getName(), e.getStackTrace());
        }

        //写入内容content到文件(自动换行)
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.valueOf(newFile.toPath())), StandardCharsets.UTF_8))) {

            bw.write(content);

        } catch (Exception e) {
            log.warn("写入文件{} 失败, errorStack{}", newFile.getName(), e.getStackTrace());
        }

        log.debug("创建文件到缓冲区{} 成功", newFile.getName());

        return newFile.toPath();
    }

    /**
     * 通知进程系统创建进程读取path文件
     *
     * @param path 文件路径
     */
    public static void notifyProcessSyS(Path path) {
        log.info("正在通知进程系统创建进程读取path文件...");
        //TODO 传递path到进程
    }

}
