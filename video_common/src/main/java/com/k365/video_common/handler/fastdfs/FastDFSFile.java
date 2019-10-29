package com.k365.video_common.handler.fastdfs;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Gavin
 * @date 2019/7/28 16:28
 * @description：
 */
@Accessors(chain = true)
@Data
public class FastDFSFile {

    private String name;
    private byte[] content;
    private String ext;
    private String md5;
    private String author;

    public FastDFSFile() {
    }

    public FastDFSFile(String name, byte[] content, String ext, String md5, String author) {
        this.name = name;
        this.content = content;
        this.ext = ext;
        this.md5 = md5;
        this.author = author;
    }

}
