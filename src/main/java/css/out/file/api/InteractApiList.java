package css.out.file.api;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用api
 */
@Slf4j
public class InteractApiList {

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

    //发送前端msg
    public static void msg(String msg) {
        String toFront = switch (msg) {
            case "0" -> "失败的请求调用";
            case "1" -> "失败的请求调用2";
            default -> "失败";
        };

        //发送
        //TODO
    }
}
