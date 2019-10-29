package com.k365.video_common.constant;

import java.awt.*;
import java.util.Random;

/**
 * @author Gavin
 * @date 2019/6/19 18:45
 * @description：验证码生成属性信息
 *
 */
public class CaptchaInfo {
    private int width = 132;
    private int height = 40;
    private int fontSize = height - 2 ;
    private Color backgroundColor = getRandColor(200,160);
    private Color jammingLineColor = getRandColor(10,5);
    private Font font = new Font("Times New Roman", Font.PLAIN, fontSize);
    private int randomCount = 5;
    private char[] code = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();

    public CaptchaInfo() {
    }

    public CaptchaInfo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public CaptchaInfo(int width, int height, int randomCount) {
        this.width = width;
        this.height = height;
        this.randomCount = randomCount;
    }

    public CaptchaInfo(int width, int height, int fontSize, Color backgroundColor, Color jammingLineColor, Font font) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.backgroundColor = backgroundColor;
        this.jammingLineColor = jammingLineColor;
        this.font = font;
    }

    public CaptchaInfo(int width, int height, Color backgroundColor, Color jammingLineColor, int randomCount) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.backgroundColor = backgroundColor;
        this.jammingLineColor = jammingLineColor;
        this.randomCount = randomCount;
    }

    public CaptchaInfo(int width, int height, int fontSize, Color backgroundColor, Color jammingLineColor, Font font, int randomCount, char[] code) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.backgroundColor = backgroundColor;
        this.jammingLineColor = jammingLineColor;
        this.font = font;
        this.randomCount = randomCount;
        this.code = code;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getJammingLineColor() {
        return jammingLineColor;
    }

    public void setJammingLineColor(Color jammingLineColor) {
        this.jammingLineColor = jammingLineColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }

    public char[] getCode() {
        return code;
    }

    public void setCode(char[] code) {
        this.code = code;
    }

    public Color getRandColor(int bc,int fc){
        Random random=new Random();
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);
        return new Color(r,g,b);
    }
}
