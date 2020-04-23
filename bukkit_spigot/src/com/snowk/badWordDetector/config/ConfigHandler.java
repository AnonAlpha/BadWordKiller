package com.snowk.badWordDetector.config;

import com.snowk.badWordDetector.BadWordDetector;

public class ConfigHandler {
	public static String msg_Reject = BadWordDetector.snowkPlugin.getConfig().getString("Msg_Reject").replace("&", "��");
	public static String msg_Reload = BadWordDetector.snowkPlugin.getConfig().getString("Msg_Reload").replace("&", "��");
	public static String msg_NoPerm = BadWordDetector.snowkPlugin.getConfig().getString("Msg_NoPerm").replace("&", "��");
	public static boolean replaceEnable = BadWordDetector.snowkPlugin.getConfig().getBoolean("replace");
	public static String maskSymbol = BadWordDetector.snowkPlugin.getConfig().getString("mask").replace("&", "��");
	public static String msg_test = BadWordDetector.snowkPlugin.getConfig().getString("Msg_test").replace("&", "��");
	public static String msg_maskChange = BadWordDetector.snowkPlugin.getConfig().getString("Msg_maskChange").replace("&", "��");
}
