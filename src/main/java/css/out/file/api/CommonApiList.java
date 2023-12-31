package css.out.file.api;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用api
 */
@Slf4j
public class CommonApiList {

    /**
     * 发送用户警告
     *
     * @param msg 警告信息
     * @return 警告信息
     */
    public static String alertUser(String msg) {
        //提示前端 TODO
        log.warn("发送用户警告: " + msg);
        return msg;
    }

    public static void operateFile() { //类型

    }
}
