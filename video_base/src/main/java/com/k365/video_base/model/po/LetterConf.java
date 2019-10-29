package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LetterConf {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String title;

    private String sendNumber;

    private String accountSid;

    private String authToken;

    private String content;

}