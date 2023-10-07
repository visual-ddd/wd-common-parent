package com.wakedata.common.core.util;

import cn.hutool.core.io.IoUtil;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class CommandUtil {

	public static int runCmd(String cmd, OutputStream output) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			if (output == null) {
				output = new ByteArrayOutputStream();
			}
			IoUtil.copy(process.getInputStream(), output);
			return process.waitFor();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static int runCmd(String cmd) {
		return runCmd(cmd, null);
	}

}