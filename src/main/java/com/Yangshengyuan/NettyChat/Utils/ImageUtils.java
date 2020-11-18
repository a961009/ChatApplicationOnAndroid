package com.Yangshengyuan.NettyChat.Utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

public class ImageUtils {
    /**
     * 按尺寸原比例缩放图片
     * @param source 输入源
     * @param output 输出源
     * @param width 256
     * @param height 256
     * @throws IOException
     */
    public static void imgThumb(String source, String output, int width, int height) throws IOException, IOException {
        Thumbnails.of(source).size(width, height).toFile(output);
    }

    /**
     * 按照比例进行缩放
     * @param source 输入源
     * @param output 输出源
     * @param scale  比例
     * @throws IOException
     */
    public static void imgScale(String source, String output, double scale) throws IOException {
        Thumbnails.of(source).scale(scale).toFile(output);
    }
}
