package css.out.file.api;

import css.out.file.enums.FileDirTYPE;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.api.InteractApiList.alertUser;

/**
 * 用户命令接口Api
 */
@Slf4j
public class OrderApiList {

    //TODO 这里的设计是只操控文件/文件夹的位置和信息, 不改变内容, 改变内容需要 打开 - 修改 - 关闭 流程, 不放在这里


    //处理命令接口信息(Service 对应 Mapper)  TODO 等待1.10号的会议, 看看怎么处理

    //TODO 接受参数确认
    public static void handleOrder(String order, String allName1, String allName2, String path1, String path2, FileDirTYPE type) {//Controller

        Object fileObjects = new Object();
        fileObjects = mkObjectfrFront(path1, allName1, type); //制作对象1 TODO

        doFunction(order, fileObjects, path2);//执行命令, 还要加一个path2, 如果没有用的话就特定标志
    }


    public static Object mkObjectfrFront(String path, String allName, FileDirTYPE type) {
        //? 制作对象
        return new Object();
    }


    //因为这些命令至多用到两个对象(文件.文件夹), 因此直接用Object接收
    public static void doFunction(String order, Object Objects, String subPath) {

        switch (order) { //根据order调用对应的方法, 根据命令的不同传递参数
            case "create" -> createOrder(Objects);
            case "copy" -> copyOrder(Objects, subPath);
            case "delete" -> deleteOrder(Objects);
            case "move" -> moveOrder(Objects, subPath);
            case "type" -> typeOrder(Objects);
            case "change" -> changeOrder(Objects, subPath);
            case "makdir" -> makdirOrder(Objects);
            case "chadir" -> chadirOrder(Objects, subPath);
            case "deldir" -> deldirOrder(Objects);
//            case "rmall" -> rmallOrder(Objects);
            case "exefile" -> exefileOrder(Objects);

            default -> {
                log.error("命令错误");
                alertUser("找不到对应的命令");
            }

        }

    }


    /**
     * 创建文件
     * <p>create XXX.XXX /tmp</p>
     *
     * @param object 文件对象
     */
    public static void createOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 复制文件
     * <p>copy XXXA.XXX /tmp /home</p>
     *
     * @param object  源文件对象
     * @param subPath 目标位置
     */
    public static void copyOrder(Object object, String subPath) {


//        handleCommon(null, null);        //开启进程
    }


    /**
     * 删除文件
     * <p>delete XXX.XXX /tmp</p>
     *
     * @param object 文件对象
     */
    public static void deleteOrder(Object object) {

    }


    /**
     * 移动文件
     * <p>move XXX.XXX /tmp /home</p>
     *
     * @param object 文件对象
     */
    public static void moveOrder(Object object, String subPath) {

//
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 展示文件
     * <p>type XXX.XXX /tmp</p>
     *
     * @param object 文件对象
     */
    public static void typeOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 修改文件名称
     * <p>change XXX.XXX /tmp YYY</p>
     *
     * @param object  文件对象
     * @param newName 新名称
     */
    public static void changeOrder(Object object, String newName) {

//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 创建文件夹
     * <p>makdir XXX.XXX /tmp </p>
     *
     * @param object 文件对象
     */
    public static void makdirOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 修改文件夹名称
     * <p>chadir XXX.XXX /tmp YYY</p>
     *
     * @param object 文件对象
     */
    public static void chadirOrder(Object object, String newName) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 删除文件夹
     * <p>deldir XXX /tmp</p>
     *
     * @param object 文件对象
     */
    public static void deldirOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 嵌套删除文件夹(以及下面的所有对象)
     * <p>rmall XXX /tmp</p>
     *
     * @param object 文件对象
     * @deprecated DLC内容 这里不建议使用嵌套回收, 因为我仍然使用的是普通的删除逻辑, 但是加上了一个类似GC的玩意在我的FAT Manager里面
     */
    public static void rmallOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 运行可执行文件
     * <p>exefile XXX.XXX /tmp</p>
     *
     * @param object 文件对象
     */
    public static void exefileOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    //TODO 这里放真正的修改文件-保存文件的流程
}
