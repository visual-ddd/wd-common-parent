package com.wakedata.common.storage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @Desc
 * @Author zkz
 * @Date 2021/12/8
 */
public interface StorageConstants {

    String SLASH = "/";

    String POINT = ".";

    String HTTPS_PREFIX = "https://";

    String QUESTION_MARK = "?";

    String AND_MARK = "&";

    /**
     * 私有bucket生成的url 10年后过期
     */
    Date EXPIRE_DAY = Date.from(Instant.now().plus(3650, ChronoUnit.DAYS));

    /**
     * 私有bucket生成的url， 亚马逊S3 7天过期
     */
    Date EXPIRE_DAY_7 = Date.from(Instant.now().plus(7, ChronoUnit.DAYS));

    /**
     * 私有bucket生成的url 10年后过期 60 * 60 *24 *3650;
     */
    long EXPIRE_SECONDS = 315360000;
}
