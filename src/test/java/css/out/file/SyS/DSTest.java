package css.out.file.SyS;

import css.out.file.FileApp;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import org.junit.Test;

import static css.out.file.entiset.GF.FILE_EXTEND;
import static css.out.file.entiset.GF.FILE_NAME_DEFAULT;
import static css.out.file.handleB.HandleFile.str2Path;
import static css.out.file.handleS.HandleDS.writeContext;

public class DSTest {

    @Test
    public void DStest() {

        FileApp app = new FileApp();

        //app.kickDiskRoboot();
//        Map<Integer, Integer> map = Map.of(
//                3, 80,
//                80, 50,
//                50, 70,
//                70, 40,
//                40, 90,
//                90, 60,
//                60, Null_Pointer
//        );
//        specifyFAT(map);


        //temp文件

        file temp_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + FILE_NAME_DEFAULT, FILE_EXTEND.get(0), "I am a winner");

        writeContext(temp_file);


        app.state();

    }
}
