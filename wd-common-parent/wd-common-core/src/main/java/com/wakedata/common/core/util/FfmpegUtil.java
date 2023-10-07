package com.wakedata.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * ffmpeg工具类
 * @author hhf
 */
public class FfmpegUtil {

	private static final Logger log = LoggerFactory.getLogger(FfmpegUtil.class);
	/**
	 * FFmpeg指令（需要打包镜像里配置了环境变量才可用）
	 */
	private static final String FFMPEG_CMD = "ffmpeg";

	public static boolean makeThumbnail(String ffmpegPath, String inputPath, String outputPath) {
		try {
			String cmd = String.format("%s -i %s -ss 0 -t 0.001 %s -y", ffmpegPath, inputPath, outputPath);
			log.info(cmd);
			return CommandUtil.runCmd(cmd, new ByteArrayOutputStream()) == 0 && new File(outputPath).exists();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}



	public static boolean makeThumbnail(String ffmpegPath, String inputPath, String outputPath, int height) {
		try {
			String cmd = String.format("%s -loglevel quiet -i %s -ss 0 -t 0.001 -vf scale=-1:%d %s -y", ffmpegPath,
					inputPath, height, outputPath);
			return CommandUtil.runCmd(cmd, new ByteArrayOutputStream()) == 0 && new File(outputPath).exists();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static boolean makeThumbnailSquare(String ffmpegPath, String inputPath, String outputPath, int height) {
		try {
			String cmd = String.format("%s -loglevel quiet -i %s -ss 0 -t 0.001 -vf scale=-1:%d,crop=ih:ih %s -y",
					ffmpegPath, inputPath, height, outputPath);
			return CommandUtil.runCmd(cmd, new ByteArrayOutputStream()) == 0 && new File(outputPath).exists();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 需要打包镜像里配置了环境变量才可用
	 */
	public static boolean makeThumbnail(String inputPath, String outputPath) {
		return makeThumbnail(FFMPEG_CMD, inputPath, outputPath);
	}

	/**
	 * 需要打包镜像里配置了环境变量才可用
	 */
	public static boolean makeThumbnail(String inputPath, String outputPath, int height) {
		return makeThumbnail(inputPath, outputPath, height);
	}
	/**
	 * 需要打包镜像里配置了环境变量才可用
	 */
	public static boolean makeThumbnailSquare(String inputPath, String outputPath, int height) {
		return makeThumbnailSquare(inputPath, outputPath, height);
	}

	public static void main(String[] args) {
		// makeThumbnail(config.getFfmpegPath(), "D:\\temp\\ff\\vdo_aap151.mp4",
		// "D:\\temp\\ff\\vdo_aap151.jpg");
		// makeThumbnail(config.getFfmpegPath(), "D:\\temp\\ff\\vdo_aap151.mp4",
		// "D:\\temp\\ff\\vdo_aap151.480.jpg", 480);
		// makeThumbnailSquare(config.getFfmpegPath(),
		// "D:\\temp\\ff\\vdo_aap151.mp4",
		// "D:\\temp\\ff\\vdo_aap151.480.square.jpg", 480);
		// extractAudio(config.getFfmpegPath(), "D:\\temp\\ff\\vdo_aap151.mp4",
		// "D:\\temp\\ff\\vdo_aap151.mp3");
	}

}