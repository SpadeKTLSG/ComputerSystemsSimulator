package css.out.file.entity;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static css.out.file.utils.GlobalField.ROOT_AUTH;

/**
 * 虚拟节点, 用于存储文件系统结构
 * 核心仍然是FCB
 */
@Data
@Slf4j
public class node {

    /**
     * 节点内包含对应文件/目录的FCB
     */
    public FCB fcb;

    /**
     * 左孩子节点
     */
    public node left;

    /**
     * 右孩子节点
     */
    public node right;

    /**
     * 节点需要绑定对应的FCB
     *
     * @param fcb 对应文件/文件夹的FCB
     */
    public node(FCB fcb) {
        this.fcb = fcb; //获取到对应对象的FCB后进行判断

        this.left = null;
        this.right = null;
    }

    public node(String auth) {
        if (auth.equals(ROOT_AUTH)) {
            this.fcb = new FCB(":/", 2, "", "DIR", 0);
        } else {
            log.error("非法的根目录创建!");
        }
    }
}
