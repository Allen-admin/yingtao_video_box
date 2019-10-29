package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * @author Gavin
 * @date 2019/8/12 16:55
 * @description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseListVO<T> {

    private Collection<T> list;

    private Long total;

}
