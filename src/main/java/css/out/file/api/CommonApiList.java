package css.out.file.api;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonApiList {

    public static String alertUser(String msg) {
        log.warn("发送用户警告: " + msg);
        return msg;
    }


}
