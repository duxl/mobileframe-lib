package com.duxl.mobileframe.util;

import java.text.SimpleDateFormat;

/**
 * 时间工具类
 * @author duxl 20151010
 *
 */
public class DateUtil {

	/**
	 * 格式化时间，把fromTime从fromFormat格式转换到toFormat格式
	 * @param fromTime 原时间
	 * @param fromFormat 原时间格式
	 * @param toFormat 需要转换成的格式
	 * @return
	 */
	public static String formatDate(String fromTime, String fromFormat, String toFormat) {
		try {
			SimpleDateFormat sdfFrom = new SimpleDateFormat(fromFormat);
			SimpleDateFormat sdfTo = new SimpleDateFormat(toFormat);
			return sdfTo.format(sdfFrom.parse(fromTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fromTime;
	}

	/**
	 * 格式化时间，把fromTime转换成toFormat格式
	 * @param fromTime 原时间
	 * @param toFormat 需要转换成的格式
	 * @return
	 */
	public static String formatDate(long fromTime, String toFormat) {
		try {
			SimpleDateFormat sdfTo = new SimpleDateFormat(toFormat);
			return sdfTo.format(fromTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fromTime+"";
	}
}
