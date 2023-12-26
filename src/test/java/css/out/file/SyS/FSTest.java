package css.out.file.SyS;

import css.out.file.FileApp;
import css.out.file.entity.dir;
import css.out.file.entity.file;
import css.out.file.enums.ROOT_PATH;
import org.junit.Test;

import static css.out.file.entiset.GF.*;
import static css.out.file.entiset.GF.DIR_EXTEND;
import static css.out.file.handleB.HandleDISK.fullFill1FAT;
import static css.out.file.handleB.HandleFile.str2Path;

public class FSTest {

    @Test
    public void FStest1_CRUD() {
        FileApp app = new FileApp();

//        app.kickDiskRoboot();
        //上上强度
        fullFill1FAT(1); //FAT1满

        //TODO 封装
        //temp文件 手动构造写入磁盘
        file temp_file = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + str2Path(DIR_NAME_DEFAULT) + ':' + FILE_NAME_DEFAULT, FILE_EXTEND.get(0), "correct input");
        file temp_fileAltered = new file(str2Path(String.valueOf(ROOT_PATH.tmp)) + str2Path(DIR_NAME_DEFAULT) + ':' + "Notebook of SpadeK", FILE_EXTEND.get(1), "I want to surpass humanity, with your blood!");
        dir temp_dir = new dir(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + DIR_NAME_DEFAULT, DIR_EXTEND.get(0));
        dir temp_dirAltered = new dir(str2Path(String.valueOf(ROOT_PATH.tmp)) + ':' + "Notebooks of SpadeK", DIR_EXTEND.get(0));


    }


}
