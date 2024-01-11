package css.out.file.api;

import css.out.file.entity.dir;
import css.out.file.entity.file;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.FileApp.selectContent;
import static css.out.file.api.InteractApiList.alertUser;

/**
 * 用户命令接口Api
 */
@Slf4j
public class OrderApiList {

    /**
     * 处理前端请求
     *
     * @param allOrder 完整前端请求
     * @return 返回处理后的对象用于进程展示
     */
    public static Object handleOrder(String allOrder) {

        //?拆分前端输入信息,使用空格
        //e.g. create XXX.XXX /tmp , 需要判断对应数量分析指令参数个数
        String[] orderList = allOrder.split(" ");

        int order_size = orderList.length;
        String order = null, allName = null, path1 = null, path2 = null;


        switch (order_size) {
            case 1, 2 -> {
                alertUser("找不到对应的命令");
                return null;
            }
            case 3 -> { //标准三个参数 -> order | 文件全名 | path1(ELSE)
                order = orderList[0];
                allName = orderList[1];
                path1 = orderList[2];

            }
            case 4 -> { //标准四个参数 -> order | 文件全名 | path1 | path2(ELSE)
                order = orderList[0];
                allName = orderList[1];
                path1 = orderList[2];
                path2 = orderList[3];
            }
            default -> {
                log.error("命令错误");
                alertUser("找不到对应的命令");
            }
        }


        if (order == null | allName == null | path1 == null) {
            alertUser("找不到对应的命令");
            return null;
        }

        Object fileObjects = mkObjectfrFront(order, allName, path1);
        if (fileObjects == null) return null; //如果构建的中间对象为空, 说明用户输入错误, 直接返回null

        doFunction(order, fileObjects, path2);      //执行命令, 还要加一个path2, 如果没有用的话就特定标志
        return fileObjects;        //?返回传递进程对象信息
    }


    public static Object mkObjectfrFront(String order, String allName, String path) {//制作对象, 需要根据操作order判断文件还是文件夹
        if (order.equals("create") | order.equals("copy") | order.equals("delete") | order.equals("move") | order.equals("type") | order.equals("change") | order.equals("run") | order.equals("edit")) {
            String pathName = path + ':' + allName.split("\\.")[0];
            String extendName = '.' + allName.split("\\.")[1];

            file temp_file = new file(pathName, extendName, "");
            return selectContent(temp_file);
        } else if (order.equals("makdir") | order.equals("chadir") | order.equals("deldir")) {
            String pathName = path + ':' + allName.split("\\.")[0];
            String extendName = ".";

            dir temp_dir = new dir(pathName, extendName);
            return selectContent(temp_dir);
        } else {
            alertUser("命令语法错误!");
            return null;
        }

    }


    public static void doFunction(String order, Object target, String subPath) {   //因为这些命令至多用到两个对象(文件.文件夹), 因此直接用Object接收

        switch (order) { //根据order调用对应的方法, 根据命令的不同传递参数
            case "create" -> createOrder(target);
            case "copy" -> copyOrder(target, subPath);
            case "delete" -> deleteOrder(target);
            case "move" -> moveOrder(target, subPath);
            case "type" -> typeOrder(target);
            case "change" -> changeOrder(target, subPath);
            case "makdir" -> makdirOrder(target);
            case "chadir" -> chadirOrder(target, subPath);
            case "deldir" -> deldirOrder(target);
            case "exefile" -> runOrder(target);
            case "edit" -> editOrder(target, subPath);    //真正的用户修改内容指令
//            case "rmall" -> rmallOrder(target);
            default -> {
                log.error("命令错误");
                alertUser("找不到对应的命令");
            }

        }

    }

    //! 以下是具体的命令实现, 每个方法注释后面标记了前端传递参数的数量 - 3 / 4, 以及对应的参数类型

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
     * 复制文件 - 4
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
     * @param object 文件夹对象
     */
    public static void makdirOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 修改文件夹名称
     * <p>chadir XXX.XXX /tmp YYY</p>
     *
     * @param object 文件夹对象
     */
    public static void chadirOrder(Object object, String newName) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 删除文件夹
     * <p>deldir XXX /tmp</p>
     *
     * @param object 文件夹对象
     */
    public static void deldirOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 运行可执行文件
     * <p>exefile XXX.XXX /tmp</p>
     *
     * @param object 文件对象
     */
    public static void runOrder(Object object) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 修改文件内容
     * <p>edit XXX.XXX asdasdasdasd</p>
     *
     * @param object 文件对象
     */
    public static void editOrder(Object object, String newContent) {
//        notifyProcessSyS(object);        //开启进程
    }


    /**
     * 嵌套删除文件夹(以及下面的所有对象)
     * <p>rmall XXX /tmp</p>
     *
     * @param object 文件夹对象
     * @deprecated DLC内容 这里不建议使用嵌套回收, 因为我仍然使用的是普通的删除逻辑, 但是加上了一个类似GC的玩意在我的FAT Manager里面
     */
    public static void rmallOrder(Object object) {
        //工期不够, 不做了
    }

}
