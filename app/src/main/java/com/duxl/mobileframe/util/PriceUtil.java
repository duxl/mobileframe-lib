package com.duxl.mobileframe.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 价格格式化类
 * @author duxl 20151007
 *
 */
public class PriceUtil {

	/**
	 * （总价）单价乘数量
	 * @param price 单价
	 * @param num 数量
	 */
	public static double multiplyPrice(double price, int num) {
		return new BigDecimal(price).multiply(new BigDecimal(num)).doubleValue();
	}

	/**
	 * (单价)总价除以数量
	 * @param price 总价
	 * @param num 数量
	 * @return
	 */
	public static double dividePrice(double price, int num) {
		if(num == 0) {
			return 0;
		}
		return new BigDecimal(price).divide(new BigDecimal(num), 10, BigDecimal.ROUND_DOWN).doubleValue();
	}
	
	/**
	 * 价格相加
	 * @param price1
	 * @param price2
	 * @return
	 */
	public static double addPrice(double price1, double price2) {
		return new BigDecimal(price1).add(new BigDecimal(price2)).doubleValue();
	}
	
	/**
	 * 价格相减
	 * @param price1
	 * @param price2
	 * @return
	 */
	public static double subtractPrice(double price1, double price2) {
		return new BigDecimal(price1).subtract(new BigDecimal(price2)).doubleValue();
	}
	
	/**
	 * 带2位小数点的格式
	 * @param price
	 * @return
	 */
	public static String formatGeneral(double price) {
		return new DecimalFormat("0.00").format(price);
	}
	
	/**
	 * 格式化价格
	 * @param price
	 * @param format
	 * @return
	 */
	public static String format(double price, String format) {
		return new DecimalFormat(format).format(price);
	}
}
