package css.out.file.SyS;

import css.out.file.FileApp;
import css.out.file.entity.dir;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import org.junit.Test;

import java.util.Map;

import static css.out.file.entiset.GF.*;
import static css.out.file.handleB.HandleDISK.*;
import static css.out.file.handleB.HandleFile.str2Path;
import static css.out.file.handleS.HandleDS.addContext;
import static css.out.file.handleS.HandleDS.deleteContext;

public class DSTest {

    @Test
    public void DStest1_CRUD() {

        FileApp app = new FileApp();

//        app.kickDiskRoboot();
        //上上强度
        fullFill1FAT(1); //FAT1满
//        fullFill1FAT(2); //全满

        Map<Integer, Integer> map = Map.of(
                3, 80,
                80, 50,
                50, 70,
                70, 40,
                40, 90,
                90, 60,
                60, Null_Pointer
        );
        specifyFAT(map);


        //temp文件 手动构造写入磁盘
        file temp_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + str2Path(DIR_NAME_DEFAULT) + ':' + FILE_NAME_DEFAULT, FILE_EXTEND.get(0), "correct input");
        dir temp_dir = new dir(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + DIR_NAME_DEFAULT, DIR_EXTEND.get(0));
        Object temp_obj = "我是一个病毒对象";

        System.out.println(temp_obj + "~我跑进来咯~");
//        System.out.println(getFATOrder());//康康FAT占用顺序

        //! CRUD

        System.out.println(getFATOrder());//康康FAT占用顺序

        addContext(temp_file);

        System.out.println(getFATOrder());//康康FAT占用顺序

        deleteContext(temp_file);

        System.out.println(getFATOrder());//康康FAT占用顺序

        addContext(temp_dir);

        System.out.println(getFATOrder());//康康FAT占用顺序

        deleteContext(temp_file);

        System.out.println(getFATOrder());//康康FAT占用顺序
//        addContext(temp_obj);
//        deleteContext(temp_obj);

        //改


        //查

        app.state();

    }
}
