package com.wakedata.common.core.constants;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.springframework.util.CollectionUtils;

/**
 * 公共常量
 *
 * @author focus
 * @date 2021/2/24
 **/
public class CommonConstant {

    public static final String OK = "OK";

    public static final String EMPTY = "";

    public static final String POINT = ".";

    public static final String COMMA = ",";

    public static final int SIXTEEN = 16;

    public static final String UNDER_LINE = "_";

    public static final String MINUS = "-";

    public static final String QUESTION = "?";

    public static final String PERCENTAGE = "%";

    public static final int FIRST_ELEMENT = 0;

    public static final int ZERO = 0;

    public static final int ONE = 1;

    public static final String JPG = "jpg";

    public static final String PNG = "png";

    public static final String COLON = ":";

    public static final String ANDSYMBOL = "&";

    public static final String IGNORE_H5_URL_MAPPING = "/h5/api";

    public static final String IMAGE_PNG = "image/png";
    public static final int BARCODE_DPI = 150;
    public static final double BARCODE_INCHES = 3.0f / BARCODE_DPI;

    /**
     * 默认的拼接字符
     */
    public static final String DEFAULT_JOIN_CHAR = ",";

    public static final class CommonCollection {

        public static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "Excel");

        public static final Set<String> IGNORE_URL_MAPPING = ImmutableSet.of(IGNORE_H5_URL_MAPPING);

        public static Boolean contain(Set<String> ignores, String uri) {
            if (CollectionUtils.isEmpty(ignores)) {
                return false;
            }
            for (String ignoreUrl : ignores) {
                if (uri.startsWith(ignoreUrl)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 会员冻结状态key
     */
    public final static String MEMBER_FROZEN_STATUS_KEY = "wd_member:frozen:unique:account:id:%s";

    public static String getMemberUniqueAccountIdStatusKey(String uniqueAccountId) {
        return String.format(MEMBER_FROZEN_STATUS_KEY, uniqueAccountId);
    }
}
