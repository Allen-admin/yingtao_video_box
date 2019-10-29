package com.k365.video_base.model.so;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Gavin
 * @date 2019/8/14 14:29
 * @description：
 */
@Data
@Accessors(chain = true)
public abstract class BaseSO {

    private Integer page = 1;

    private Integer pageSize = 10;

    private String sortName;

    private Boolean isAsc = true;

}
