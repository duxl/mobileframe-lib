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
		return new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(String.valueOf(num))).doubleValue();
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
		return new BigDecimal(String.valueOf(price)).divide(new BigDecimal(String.valueOf(num)), 10, BigDecimal.ROUND_DOWN).doubleValue();
	}
	
	/**
	 * 价格相加
	 * @param price1
	 * @param price2
	 * @return
	 */
	public static double addPrice(double price1, double price2) {
		return new BigDecimal(String.valueOf(price1)).add(new BigDecimal(String.valueOf(price2))).doubleValue();
	}
	
	/**
	 * 价格相减
	 * @param price1
	 * @param price2
	 * @return
	 */
	public static double subtractPrice(double price1, double price2) {
		return new BigDecimal(String.valueOf(price1)).subtract(new BigDecimal(String.valueOf(price2))).doubleValue();
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

	/**
     *
     * <pre>
     *     万以下原样显示，万以上用万做单位，保留两位小数四舍五入
     *     12       →   12
     *     1234     →   1,234
     *     12.345   →   12.35
     *     1230000  →   123万
     *     1234567  →   123.46万
     *     12345678 →   1,234.57万
     * </pre>
     * @param value 格式化的数字
     * @return
     */
    public static String formatW(Double value) {
	if(value == null) {
	    return "0";	
	}
        StringBuffer sb = new StringBuffer();

        BigDecimal wan = new BigDecimal("10000"); // 万
        BigDecimal num = new BigDecimal(value);

        DecimalFormat df = new DecimalFormat(); // 小数位末尾是0的舍去
        // DecimalFormat df = new DecimalFormat("0.00"); // 始终保留2位小数
        // df.setMinimumFractionDigits(2); // 至少保留2位小数
	// df.setNegativePrefix("复"); // 设置负数前缀
        df.setMaximumFractionDigits(2); // 最多保留2位小数
        df.setGroupingSize(3); // 整数位3位分为一组
        df.setRoundingMode(RoundingMode.HALF_UP); // 四舍五入

        if (num.compareTo(wan) == -1 || num.compareTo(wan) == 0) { // 万及以下
            sb.append(df.format(num));

        } else { // 万以上
            sb.append(df.format(num.divide(wan)));
            sb.append("万");

        }

        if (sb.length() == 0) {
            return "0";
        }

        return sb.toString();
    }
}
