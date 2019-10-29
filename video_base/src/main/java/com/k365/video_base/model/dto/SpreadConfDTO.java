package com.k365.video_base.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpreadConfDTO extends SplitPageDTO implements BaseDTO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String url;

    private String title;

    private String explain;

    private Boolean isShow;

    private String content;

}