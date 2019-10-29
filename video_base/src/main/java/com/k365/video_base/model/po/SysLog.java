package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NegativeOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SysLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作时间
     */
    private Date time;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户ip
     */
    private String userIp;

    /**
     * 操作功能
     */
    private String operate;

    /**
     * 是否成功（true-成功，false-失败）
     */
    private Boolean status;

    /**
     * 访问路径
     */
    private String accessPath;


}
