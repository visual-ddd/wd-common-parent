package com.wakedata.common.core.util;

import cn.hutool.core.codec.Base64;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 图片处理
 *
 * @author zkz
 * @date 2021/12/13
 */
public class ImageUtil {

    private static final String HTTP = "http";

    private ImageUtil() {

    }

    /**
     * 生成缩略图
     *
     * @param width  宽度
     * @param height 高度
     */
    public static ByteArrayOutputStream thumbnail(int width, int height, InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream).size(width, height).toOutputStream(outputStream);
        return outputStream;
    }

    /**
     * 读取图片
     *
     * @param image 图片对象,http开头为从网络获取,否则从文件获取
     * @return 图片对象
     */
    public static BufferedImage readImage(String image) throws Exception {
        if (image.startsWith(HTTP)) {
            return readImageFromUrl(image);
        } else {
            return readImageFromFile(image);
        }
    }

    /**
     * 从网络读取图片
     *
     * @param url 图片url
     * @return 图片对象
     * @throws Exception 可能会读取异常
     */
    public static BufferedImage readImageFromUrl(String url) throws Exception {
        return ImageIO.read(new URL(url));
    }

    /**
     * 从本地文件读取图片
     *
     * @param filePath 文件路径
     * @return 图片对象
     * @throws IOException 可能会读取异常
     */
    public static BufferedImage readImageFromFile(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    /**
     * 从文件读取图片
     *
     * @param file 文件对象
     * @return 图片对象
     * @throws IOException 可能会读取异常
     */
    public static BufferedImage readImageFromFile(File file) throws IOException {
        return ImageIO.read(file);
    }

    /**
     * 按比例裁剪图片
     *
     * @param source 待处理的图片流
     * @param startX 开始x坐标
     * @param startY 开始y坐标
     * @param endX   结束x坐标
     * @param endY   结束y坐标
     * @return 裁剪过的图片
     */
    public static BufferedImage crop(BufferedImage source,
                                     int startX, int startY,
                                     int endX, int endY) {
        int width = source.getWidth();
        int height = source.getHeight();
        if (startX <= -1) {
            startX = 0;
        }
        if (startY <= -1) {
            startY = 0;
        }
        if (endX <= -1) {
            endX = width;
        }
        if (endY <= -1) {
            endY = height;
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, BufferedImage.TYPE_4BYTE_ABGR);
        result.createGraphics().setBackground(new Color(0x00ffffff, true));
        for (int y = startY; y < endY + startY; y++) {
            for (int x = startX; x < endX + startX; x++) {
                //如果在源文件范围内,取源文件颜色,否则取透明色
                try {
                    int rgb = source.getRGB(x, y);
                    result.setRGB(x - startX, y - startY, rgb);
                } catch (Exception e) {
                    //数组越界就留透明色
                }
            }
        }
        return result;
    }

    /**
     * 按宽高拉伸图片
     *
     * @param image   图片
     * @param targetW 目标宽度
     * @param targetH 目标高度
     * @return 变更尺寸后的图片
     */
    public static BufferedImage fill(String image, int targetW, int targetH) throws Exception {
        return resize(readImage(image), targetW, targetH, false);
    }

    /**
     * 按宽高拉伸图片
     *
     * @param source  图片
     * @param targetW 目标宽度
     * @param targetH 目标高度
     * @return 变更尺寸后的图片
     */
    public static BufferedImage fill(BufferedImage source, int targetW, int targetH) {
        return resize(source, targetW, targetH, false);
    }

    /**
     * 按宽高等比缩放图片
     *
     * @param image   图片
     * @param targetW 目标宽度
     * @param targetH 目标高度
     * @return 变更尺寸后的图片
     */
    public static BufferedImage scale(String image, int targetW, int targetH) throws Exception {
        return resize(readImage(image), targetW, targetH, true);
    }

    /**
     * 按宽高等比缩放图片
     *
     * @param source  图片
     * @param targetW 目标宽度
     * @param targetH 目标高度
     * @return 变更尺寸后的图片
     */
    public static BufferedImage scale(BufferedImage source, int targetW, int targetH) {
        return resize(source, targetW, targetH, true);
    }

    /**
     * 变更图片尺寸
     *
     * @param source  图片
     * @param targetW 目标宽度
     * @param targetH 目标高度
     * @param scale   是否按比例缩放
     * @return 变更后图片
     */
    private static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean scale) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
        // 则将下面的if else语句注释即可
        if (scale) {
            if (sx < sy) {
                sx = sy;
                targetW = (int) (sx * source.getWidth());
            } else {
                sy = sx;
                targetH = (int) (sy * source.getHeight());
            }
        }
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
                    targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else {
            target = new BufferedImage(targetW, targetH, type);
        }
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * 图片报存到文件
     *
     * @param image 图片对象
     * @param path  保存路径
     * @return 保存结果, 成功或失败
     */
    public static boolean save(BufferedImage image, String path) {
        try {
            File tmpFile = new File(path);
            return ImageIO.write(image, "png", tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 图片转base64
     *
     * @param image 图片对象
     * @return base64
     * @throws IOException 可能会读取错误
     */
    public static String toBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        return String.format("data:image/png;base64,%s", Base64.encode(stream.toByteArray()));
    }
}
