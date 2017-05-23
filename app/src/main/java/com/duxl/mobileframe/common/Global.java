package com.duxl.mobileframe.common;

/**
 * Created by duxl on 2016/2/19.
 */
public class Global {

    public static final boolean DEBUG = true;

    /**
     * 正则表达式规则定义
     */
    public static final class RegExp {

        /**
         * 只能是字母和中文
         */
        public static final String LetterOrChinese = "^[a-zA-Z\u4e00-\u9fa5]*$";

        /**
         * 密码规则：字母、数字、下划线(6-32位)
         */
        public static final String PWD = "^[A-Za-z0-9_]{6,32}$";

        /**
         * 简单手机号码有效验证（13/14/15/17/18开头的11位数字有效）
         */
        public static final String PHONE = "^(13|14|15|17|18)\\d{9}$";

        /**
         * 身份证号码：15位或18位数字（或x结尾）。<br/><br/>严格验证请使用 {@link com.duxl.mobileframe.util.IDCardVeryer IDCardVeryer}
         */
        @Deprecated
        public static final String IDCARD = "^(\\d{14}|\\d{17})(\\d|[xX])$";

        /**
         * 邮箱验证规则
         */
        public static final String EMAIL = "^[0-9a-zA-Z]+@[0-9a-zA-Z]+\\.[0-9a-zA-Z]+$";

    }

}
