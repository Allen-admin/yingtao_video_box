package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoImport {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 导入成功的总数
     */
    private Integer importOkCount;

    /**
     *导入总数
     */
    private Integer importCount;

    /**
     * 导入失败视频标题
     */
    private String failTitles;

    /**
     * 文件名
     */
    private String fileName;

}


