package com.k365.video_common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.k365.video_common.handler.fastdfs.FastDFSFile;
import com.k365.video_common.handler.fastdfs.FastDFSHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/21 11:36
 * @description：
 */
public class QRcodeUtil {
    private static final int width = 300;// 默认二维码宽度
    private static final int height = 300;// 默认二维码高度
    private static final String format = "png";// 默认二维码文件格式
    private static final Map<EncodeHintType, Object> hints = new HashMap();// 二维码参数

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        hints.put(EncodeHintType.MARGIN, 2);// 二维码与图片边距
    }

    /**
     * 返回一个 BufferedImage 对象
     *
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     */
    public static BufferedImage toBufferedImage(String content, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 将二维码图片输出到一个流中
     *
     * @param content 二维码内容
     * @param stream  输出流
     * @param width   宽
     * @param height  高
     */
    public static void writeToStream(String content, OutputStream stream, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
    }

    /**
     * 将二维码图片输出到一个流中
     *
     * @param content 二维码内容
     * @param stream  输出流
     */
    public static void writeToStream(String content, OutputStream stream) throws WriterException, IOException {
        writeToStream(content, stream,width, height);
    }

    /**
     * 生成二维码图片文件
     *
     * @param content 二维码内容
     * @param path    文件保存路径
     * @param width   宽
     * @param height  高
     */
    @SuppressWarnings("all")
    public static void createQRCode(String content, String path, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        //toPath() 方法由 jdk1.7 及以上提供
        MatrixToImageWriter.writeToPath(bitMatrix, format, new File(path).toPath());
    }




    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 296;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    /** 生成二维码 */
    private static BufferedImage createImage(String content,InputStream logo)throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // 插入图片
        QRcodeUtil.insertImage(image, logo, true);
        return image;
    }


    /** 在二维码中间插入logo图片 */
    private static void insertImage(BufferedImage source, InputStream logo, boolean needCompress)throws Exception {
        if(logo==null)return;
        Image src = ImageIO.read(logo);
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 生成二维码
     * @param content 文本内容
     * @param logo 内嵌图标
     * @param output 输出流
     * @throws Exception
     */
    public static void encode(String content, InputStream logo, OutputStream output) throws Exception {
        BufferedImage image = QRcodeUtil.createImage(content, logo);
        ImageIO.write(image, FORMAT_NAME, output);
    }
    public static void encode(String content, InputStream logo, File out)throws Exception {
        if(!out.getParentFile().exists()){
            out.getParentFile().mkdirs();
        }
        out.createNewFile();
        QRcodeUtil.encode(content, logo, new FileOutputStream(out));
    }
    public static void encode(String content, File logo, OutputStream out)throws Exception {
        if(logo.exists()){
            QRcodeUtil.encode(content, new FileInputStream(logo), out);
        }else{
            QRcodeUtil.encode(content,(InputStream)null, out);
        }
    }
    public static void encode(String content, File logo, File out)throws Exception {
        if(!out.getParentFile().exists()){
            out.getParentFile().mkdirs();
        }
        out.createNewFile();
        if(logo.exists()){
            QRcodeUtil.encode(content, new FileInputStream(logo), out);
        }else{
            QRcodeUtil.encode(content,(InputStream)null, out);
        }
    }
    public static void encode(String content, InputStream logo, String out)throws Exception {
        QRcodeUtil.encode(content, logo, new File(out));
    }
    public static void encode(String content, String logo, String out) throws Exception {
        QRcodeUtil.encode(content, new File(logo), new File(out));
    }
    public static void encode(String content, OutputStream out) throws Exception {
        QRcodeUtil.encode(content, (InputStream)null, out);
    }
    public static void encode(String content, String out)throws Exception {
        QRcodeUtil.encode(content,(InputStream)null, new File(out));
    }




    /**
     * 解析二维码,input是二维码图片流
     */
    public static String decode(InputStream input) throws Exception {
        BufferedImage image = ImageIO.read(input);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }
    /**
     * 解析二维码,file是二维码图片
     */
    public static String decode(File file) throws Exception{
        return decode(new FileInputStream(file));
    }
    /**
     * 解析二维码,path是二维码图片路径
     */
    public static String decode(String path) throws Exception {
        return QRcodeUtil.decode(new File(path));
    }

    public static void main(String[] args) throws Exception {
        QRcodeUtil.encode("https://www.aihe88.com/",
                FastDFSHelper.downloadFile("group1","M00/00/00/Z2SO0F1mcICAQE8CABAAAIP_cnA339.jpg"),
                new File("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\qr.png"));
//        String data = QRcodeUtil.decode("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\qr.png");
//        System.out.println(data);
    }

    private static class BufferedImageLuminanceSource extends LuminanceSource {
        private final BufferedImage image;
        private final int left;
        private final int top;

        public BufferedImageLuminanceSource(BufferedImage image) {
            this(image, 0, 0, image.getWidth(), image.getHeight());
        }

        public BufferedImageLuminanceSource(BufferedImage image, int left, int top, int width,
                                            int height) {
            super(width, height);

            int sourceWidth = image.getWidth();
            int sourceHeight = image.getHeight();
            if (left + width > sourceWidth || top + height > sourceHeight) {
                throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
            }

            for (int y = top; y < top + height; y++) {
                for (int x = left; x < left + width; x++) {
                    if ((image.getRGB(x, y) & 0xFF000000) == 0) {
                        image.setRGB(x, y, 0xFFFFFFFF); // = white
                    }
                }
            }

            this.image = new BufferedImage(sourceWidth, sourceHeight, BufferedImage.TYPE_BYTE_GRAY);
            this.image.getGraphics().drawImage(image, 0, 0, null);
            this.left = left;
            this.top = top;
        }

        public byte[] getRow(int y, byte[] row) {
            if (y < 0 || y >= getHeight()) {
                throw new IllegalArgumentException("Requested row is outside the image: " + y);
            }
            int width = getWidth();
            if (row == null || row.length < width) {
                row = new byte[width];
            }
            image.getRaster().getDataElements(left, top + y, width, 1, row);
            return row;
        }

        public byte[] getMatrix() {
            int width = getWidth();
            int height = getHeight();
            int area = width * height;
            byte[] matrix = new byte[area];
            image.getRaster().getDataElements(left, top, width, height, matrix);
            return matrix;
        }

        public boolean isCropSupported() {
            return true;
        }

        public LuminanceSource crop(int left, int top, int width, int height) {
            return new BufferedImageLuminanceSource(image, this.left + left, this.top + top, width,
                    height);
        }

        public boolean isRotateSupported() {
            return true;
        }

        public LuminanceSource rotateCounterClockwise() {
            int sourceWidth = image.getWidth();
            int sourceHeight = image.getHeight();
            AffineTransform transform = new AffineTransform(0.0, -1.0, 1.0, 0.0, 0.0, sourceWidth);
            BufferedImage rotatedImage = new BufferedImage(sourceHeight, sourceWidth,
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = rotatedImage.createGraphics();
            g.drawImage(image, transform, null);
            g.dispose();
            int width = getWidth();
            return new BufferedImageLuminanceSource(rotatedImage, top,
                    sourceWidth - (left + width), getHeight(), width);
        }
    }

}
