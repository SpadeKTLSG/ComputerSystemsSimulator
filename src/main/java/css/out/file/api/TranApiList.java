package css.out.file.api;

import java.util.Arrays;

import static css.out.file.FileApp.fileSyS;

public class TranApiList {


    public static void getTargetPathfromFront() {


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


}
