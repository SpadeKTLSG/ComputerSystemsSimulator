package css.out.file.api;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static css.out.file.FileApp.fileSyS;
import static css.out.file.entiset.GF.Null_Pointer;
import static css.out.file.handleB.HandleDISK.mergeFATs;

/**
 * 模块间信息通讯api
 */
@Slf4j
public class TranApiList {

    //从前端接收请求, 一般是指令内容 / 重载有指令内容信息的类型
    public static void getFrontRequest(String order) {
        //TODO
        //根据order调用对应的方法, 根据命令的不同传递参数
//        handleOrder();
        //通用操作处理
//        handleCommon();
    }

    public static void getFrontRequest(String order, String content) {
        //TODO
    }

    
    public static void getmFrontTargetPath() {
//和下面一样需要刷洗转换

    }


    /**
     * 传递路径给前端
     * <p>[/, /:home, /:app, /:tmp, /:conf,.....]</p>
     *
     * @return String[] pathList
     */
    public static String[] givePath2Front() {
        //?foreach 读取PM内的项目, 打包到String[]

        String[] pathList = new String[fileSyS.pathManager.size()];

        //从PM中选取所有非""的项目, 封装到String[]中, 传递给Front
        String[] finalPathList = pathList;
        fileSyS.pathManager.forEach((k, v) -> {
            if (!v.equals(" ")) finalPathList[Integer.parseInt(String.valueOf(k))] = v;
        });


        pathList = Arrays.stream(pathList)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        //?刷洗符合要求
        for (int i = 0; i < pathList.length; i++) {
            String e = pathList[i];
            if (e.equals("/")) {
                pathList[i] = "";
            } else if (e.startsWith("/")) {
                pathList[i] = e.substring(1);
            }
            if (e.contains(":")) {
                pathList[i] = e.replace(":", "");
            }
        }

        //刷洗空值
        pathList = Arrays.stream(pathList)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);


        return pathList;
    }


    /**
     * 传递磁盘占用情况给前端
     *
     * @return DTO FAT
     */
    public static List<Integer> giveBlockStatus2Front() {
        List<Integer> greatFAT = mergeFATs();
        //刷洗greatFAT, 0: 空闲 1:占用 -> DTO FAT
        //?刷洗符合要求
        for (int i = 0; i < greatFAT.size(); i++) {
            Integer e = greatFAT.get(i);
            if (e.equals(Null_Pointer)) {
                greatFAT.set(i, 0);
            } else {
                greatFAT.set(i, 1);
            }
        }
        return greatFAT;
    }

}
