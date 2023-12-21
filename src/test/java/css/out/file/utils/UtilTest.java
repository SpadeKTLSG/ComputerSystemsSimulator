package css.out.file.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static css.out.file.handle.HandleDISK.writeStr2Disk;

@Slf4j
public class UtilTest {

    @Test
    public void IOtest() {
        log.debug("测试工具类");

        //测试精确替换
        String path = "D:\\Workshop\\Codes\\JA\\ComputerSystemsSimulator\\src\\test\\resources\\common\\file\\temp.txt";
        writeStr2Disk("1234567890", path, 11);

        //测试能否覆盖写入
    }
}
