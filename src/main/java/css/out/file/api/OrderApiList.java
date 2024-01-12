package css.out.file.api;

import css.out.file.entity.dir;
import css.out.file.entity.file;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.FileApp.addContent;
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
                log.warn("命令错误");
                alertUser("找不到对应的命令");
            }
        }


        if (order == null | allName == null | path1 == null) {
            alertUser("找不到对应的命令");
            return null;
        }


        //执行命令, 还要加一个path2, 如果没有用的话就特定标志
        return doFunction(order, allName, path1, path2);          //?返回传递进程对象信息
    }


    public static Object doFunction(String order, String allName, String path, String subPath) {   //因为这些命令至多用到两个对象(文件.文件夹), 因此直接用Object接收

        switch (order) { //根据order调用对应的方法, 根据命令的不同传递参数
            case "create" -> {
                return createOrder(packageObjectfrFront(order, allName, path));
            }
            case "copy" -> {
                return copyOrder(selectObjectfrFront(order, allName, path), subPath);
            }
            /*case "delete" -> {
                return deleteOrder(target);
            }
            case "move" -> {
                return moveOrder(target, subPath);
            }
            case "type" -> {
                return typeOrder(target);
            }
            case "change" -> {
                return changeOrder(target, subPath);
            }
            case "makdir" -> {
                return makdirOrder(target);
            }
            case "chadir" -> {
                return chadirOrder(target, subPath);
            }
            case "deldir" -> {
                return deldirOrder(target);
            }
            case "exefile" -> {
                return runOrder(target);
            }
            case "edit" -> {
                return editOrder(target, subPath);    //真正的用户修改内容指令
            }*/
            default -> {
                log.error("命令错误");
                alertUser("找不到对应的命令");
                return null;
            }

        }

    }


    //? 两种不同的对象处理方式, 一种是定位到现有对象, 一种是制作新对象

    /**
     * 定位到现有对象完成DTO传递
     *
     * @param order   操作指令
     * @param allName 文件全名
     * @param path    文件路径
     * @return 打包对象
     */
    public static Object selectObjectfrFront(String order, String allName, String path) {//定位到现有对象, 需要根据操作order判断文件还是文件夹
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

    /**
     * 制作新对象完成DTO传递
     *
     * @param order   操作指令
     * @param allName 文件全名
     * @param path    文件路径
     * @return 新对象
     */
    public static Object packageObjectfrFront(String order, String allName, String path) {//制作新对象对象, 需要根据操作order判断文件还是文件夹
        if (order.equals("create") | order.equals("copy") | order.equals("delete") | order.equals("move") | order.equals("type") | order.equals("change") | order.equals("run") | order.equals("edit")) {
            file temp_file = new file(path + ':' + allName.split("\\.")[0], '.' + allName.split("\\.")[1], "");
            addContent(temp_file);
            return selectContent(temp_file);

        } else if (order.equals("makdir") | order.equals("chadir") | order.equals("deldir")) {
            dir temp_dir = new dir(path + ':' + allName.split("\\.")[0], ".");
            addContent(temp_dir);
            return selectContent(temp_dir);

        } else {
            alertUser("命令语法错误!");
            return null;
        }
    }

    //! 以下是具体的命令实现, 每个方法注释后面标记了前端传递参数的数量 - 3 / 4, 以及对应的参数类型

    /**
     * 创建文件
     * <p>create XXX.XXX /tmp</p>
     *
     * @param object 源文件对象
     * @return 返回工作对象给进程
     */
    public static Object createOrder(Object object) {
        return object;
    }


    /**
     * 复制文件 - 4
     * <p>copy XXXA.XXX /tmp /home</p>
     *
     * @param A       源文件对象
     * @param subPath 目标位置
     */
    public static Object copyOrder(Object A, String subPath) {
        file or_file = (file) A;
        file B = new file(subPath + ':' + or_file.fcb.pathName.split(":")[1], or_file.fcb.getExtendName(), or_file.getContent());
        addContent(B);
        return B;
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
