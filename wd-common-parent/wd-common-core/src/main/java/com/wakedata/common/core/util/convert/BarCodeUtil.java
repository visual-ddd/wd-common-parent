package com.wakedata.common.core.util.convert;

import com.wakedata.common.core.constants.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Classname BarCodeUtil
 * @Description 条形码工具类
 * @Date 2022/6/8 14:52
 * @Author LinXuPei
 */
public class BarCodeUtil {
    /**
     * 生成文件
     *
     * @param msg  输入信息
     * @param path 存储路径
     * @return 文件
     */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file), null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成文件
     *
     * @param msg      输入信息
     * @param path     存储路径
     * @param hideText 是否隐藏条形码文字
     * @return 文件
     */
    public static File generateFile(String msg, String path, Boolean hideText) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file), hideText);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成字节
     *
     * @param msg 输入信息
     * @return 字节数组
     */
    public static byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous, null);
        return ous.toByteArray();
    }

    /**
     * 生成到流
     *
     * @param msg      输入信息
     * @param ous      输出流
     * @param hideText 是否隐藏条形码文字
     */
    public static void generate(String msg, OutputStream ous, Boolean hideText) {
        if (StringUtils.isEmpty(msg) || ous == null) {
            return;
        }

        Code128Bean bean = new Code128Bean();
        // module宽度
        final double moduleWidth = UnitConv.in2mm(CommonConstant.BARCODE_INCHES);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.doQuietZone(Boolean.FALSE);
        if (Boolean.TRUE.equals(hideText)) {
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        }

        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, CommonConstant.IMAGE_PNG, CommonConstant.BARCODE_DPI,
                    BufferedImage.TYPE_BYTE_BINARY, Boolean.FALSE, CommonConstant.ZERO);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成到流
     *
     * @param msg 输入信息
     * @param ous 输出流
     */
    public static void generate(String msg, OutputStream ous) {
        if (StringUtils.isEmpty(msg) || ous == null) {
            return;
        }

        Code128Bean bean = new Code128Bean();
        // module宽度
        final double moduleWidth = UnitConv.in2mm(CommonConstant.BARCODE_INCHES);

        // 配置对象
        bean.setModuleWidth(moduleWidth);
        bean.doQuietZone(Boolean.FALSE);

        try {

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, CommonConstant.IMAGE_PNG, CommonConstant.BARCODE_DPI,
                    BufferedImage.TYPE_BYTE_BINARY, Boolean.FALSE, CommonConstant.ZERO);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
