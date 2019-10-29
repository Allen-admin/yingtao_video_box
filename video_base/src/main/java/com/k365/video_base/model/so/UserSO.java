package com.k365.video_base.model.so;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/30 10:36
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSO extends BaseSO{

    private String id;


    private String macAddr;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;


}
