package com.k365.global.impl;

import com.k365.global.CommonService;
import com.k365.video_base.common.CustomAttr;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.handler.fastdfs.FastDFSFile;
import com.k365.video_common.handler.fastdfs.FastDFSHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gavin
 * @date 2019/8/27 14:45
 * @description：
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CustomAttr attr;

    @Override
    public String uploadFile(MultipartFile file, String oldFilePath) {
        if(file == null)
            throw new ResponsiveException("未获取到您所上传的文件");

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] data = FastDFSHelper.getFileBuff(file);
        if (data == null)
            throw new ResponsiveException("不能上传空文件");

        FastDFSFile dfsFile = new FastDFSFile();
        dfsFile.setExt(ext);
        dfsFile.setContent(data);

        String[] uploadFile = FastDFSHelper.uploadFile(dfsFile);
        if (uploadFile == null || uploadFile.length < 2) {
            throw new ResponsiveException("文件保存失败");
        }

        String path = StringUtils.join(attr.getFastdfsServer(),"/", uploadFile[0], "/", uploadFile[1]);
        if(StringUtils.isNotBlank(oldFilePath)&&!oldFilePath.equals("null")){
            String[] dfsPath = oldFilePath.split("/", 5);
            FastDFSHelper.deleteFile(dfsPath[3], dfsPath[4]);
        }
        return path;
    }
}
