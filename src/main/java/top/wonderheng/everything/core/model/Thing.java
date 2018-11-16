package top.wonderheng.everything.core.model;

import lombok.Data;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.model
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:28
 */
@Data
public class Thing {

    private String name;

    private ThingType thingType;

    private FileType fileType;

    private String path;

    private int depth;

    private String pinyin;

}
