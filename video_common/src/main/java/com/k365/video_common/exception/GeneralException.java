package com.k365.video_common.exception;


/**
 * @author Gavin
 * @date 2019/6/22 20:15
 * @description：
 */
public class GeneralException extends RuntimeException{

    private static final long serialVersionUID = 7738287753871324101L;

    public GeneralException(String explain,Exception e) {
        super(explain+" : "+e.getMessage(),e);
    }

}