package com.duxl.mobileframe.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duxiangliang on 2015/9/1.
 */
public class StringUtils {

    /**
     * 正则验证
     * @param context 要验证的内容
     * @param regExp 验证规则,可参见 {@link com.duxl.mobileframe.common.Global.RegExp RegExp}
     *
     * @return 通过true，否则false
     */
    public static boolean regExpVerify(String context, String regExp) {
        Pattern p = Pattern.compile(regExp);
        Matcher matcher = p.matcher(context);
        if(matcher.find()){
            return true;
        }
        return false;
    }

    /**
     * 格式化字符串，将空指针的Null转换成双引号的空字符串
     * @param text
     * @return
     */
    public static String formatNull(String text) {
        if(text == null) {
            return "";
        }
        return text;
    }

    /**
     * 给文字添加删除线
     * @param text 需要添加删除线的文字
     * @param start 删除线的开始位置
     * @param end 删除线的结束位置
     * @return
     */
    public static SpannableString strikeThrough(CharSequence text, int start, int end) {
        SpannableString spanStr = new SpannableString(text);
        spanStr.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 给文字着色
     * @param text 需要着色的文字
     * @param color 颜色
     * @param start 着色开始位置
     * @param end 着色结束位置
     * @return
     */
    public static SpannableString foregroundColor(CharSequence text, int color, int start, int end) {
        SpannableString spanStr = new SpannableString(text);
        spanStr.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    /**
     * 获取富文本
     *
     * @param context
     * @param iconResid 图片资源id
     * @param text 原字符串
     * @param replaceText 原字符串中需要替换成图片的字符
     * @return
     */
    public static SpannableString getRichText(Context context, int iconResid, String text, String replaceText) {
        Drawable drawable = context.getResources().getDrawable(iconResid);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        return getRichText(drawable, text, replaceText);
    }

    /**
     * 获取富文本
     *
     * @param drawable 图片
     * @param text 原字符串
     * @param replaceText 原字符串中需要替换成图片的字符
     * @return
     */
    public static SpannableString getRichText(Drawable drawable, String text, String replaceText) {
        SpannableString spannable = new SpannableString(text);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        // 替换 replaceText的位置为相应的图片
        int start = text.indexOf(replaceText);
        int end = start + replaceText.length();
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 隐藏手机号码
     * @param phone 原号码，如13512343379
     * @return 把号码中间部分改为星号，例如135****3379
     */
    public static String secrecyPhone(String phone) {
        if(TextUtils.isEmpty(phone)) {
            return phone;
        }

        char[] ch = phone.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<ch.length; i++) {
            if(i >2 && i<7) {
                sb.append("*");
            } else {
                sb.append(ch[i]);
            }
        }
        return sb.toString();
    }
}
