package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserActionAnalyze {

    @TableId(value = "id", type = IdType.UUID)
    private String id;


    /**
     * 用户mac地址
     */
    private String macAddr;

    /**
     * 视频标签id
     */
    private String videoLabelId;

    /**
     * 创建时间
     */
    private Date crttime;



}
