package com.k365.global;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gavin
 * @date 2019/8/27 14:45
 * @description：
 */
public interface CommonService {

    /**
     * 上传文件同时删除旧文件
     * @param file
     * @param oldFilePath
     * @return
     */
    String uploadFile(MultipartFile file, String oldFilePath);
}
