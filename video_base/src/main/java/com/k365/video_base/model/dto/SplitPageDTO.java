package com.k365.video_base.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Gavin
 * @date 2019/6/30 10:39
 * @description：
 */
@Data
@Accessors(chain = true)
public abstract class SplitPageDTO {
    private Integer page = 1;

    private Integer pageSize = 10;

    private Boolean asc = true;

}
