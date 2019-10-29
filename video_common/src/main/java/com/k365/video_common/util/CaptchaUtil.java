package com.k365.video_common.util;

import com.k365.video_common.constant.CaptchaInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author Gavin
 * @date 2019/6/19 18:24
 * @description：验证码生成工具类
 */
public class CaptchaUtil {

    /**
     * 生成验证码参数信息
     */
    private CaptchaInfo captchaInfo;
    /**
     * 验证码图片
     */
    private BufferedImage captchaImage;
    /**
     * 验证码
     */
    private String captchaCode;


    private CaptchaUtil(CaptchaInfo captchaInfo) {
        this.captchaInfo = captchaInfo;
        init();
    }

    public static CaptchaUtil Instance() {

        return Instance(new CaptchaInfo());
    }

    public static CaptchaUtil Instance(CaptchaInfo captchaInfo) {

        return new CaptchaUtil(captchaInfo);
    }

    public BufferedImage getCaptchaImage() {

        return captchaImage;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    private void init() {

        int width = captchaInfo.getWidth();
        int height = captchaInfo.getHeight();
        int fontSize = captchaInfo.getFontSize();
        Color backgroundColor = captchaInfo.getBackgroundColor();
        Color jammingLineColor = captchaInfo.getJammingLineColor();
        Font font = captchaInfo.getFont();
        int randomCount = captchaInfo.getRandomCount();
        char[] code = captchaInfo.getCode();
        // 在内存中创建图象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics graphics = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        //设定背景色
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        //设定字体
        graphics.setFont(font);
        // 随机产生50条干扰线，使图象中的认证码不易被其它程序探测到
        graphics.setColor(jammingLineColor);
        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(width / 4 - 2 ) + 2 ;
            int y = random.nextInt(height - 4) + 2;
            int x1 = random.nextInt(width /4 ) + width / 4 * 3 -2 ;
            int y1 = random.nextInt(height - 3) + 1;
            graphics.drawLine(x, y,  x1, y1);
        }

        String sRand = "";
        for (int i = 0; i < randomCount; i++) {
            String rand = String.valueOf(code[random.nextInt(code.length)]);
            sRand += rand;
            // 将认证码显示到图象中
            graphics.setColor(jammingLineColor);
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            graphics.drawString(rand, i * (width / (randomCount + 1)) + 4, height - 4 );
        }
        this.captchaCode = sRand;
        // 图象生效
        graphics.dispose();
        this.captchaImage = image;  // 赋值图像
    }

}
