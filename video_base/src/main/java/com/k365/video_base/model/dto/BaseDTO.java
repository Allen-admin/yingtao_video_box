package com.k365.video_base.model.dto;

import lombok.Builder;

/**
 * @author Gavin
 * @date 2019/6/29 21:07
 * @description：
 */

public interface BaseDTO {
    interface Add extends Builder.Default {}

    interface Update extends Builder.Default {}

    interface Login extends Builder.Default {}

    interface Remove extends Builder.Default{}
}
