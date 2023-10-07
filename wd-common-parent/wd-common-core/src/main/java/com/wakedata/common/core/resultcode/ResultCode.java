package com.wakedata.common.core.resultcode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * 返回状态码
 *
 * @author hhf
 * @date 2021/1/27
 */
@Slf4j
public abstract class ResultCode {

    // 范围声明
    static {
        // 系统全局码，从0开始，step=1000
        ResponseCodeContainer.register(CommonResultCode.class, 0, 10000);
        //业务异常一共6位，前两位代表业务,例如10,0001代表permission返回的业务code



    }

    private Integer code;

    private String msg;

    private Boolean success;

    private ResultCode() {
    }

    protected ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        ResponseCodeContainer.put(this);
    }

    protected ResultCode(Integer code, String msg, Boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        ResponseCodeContainer.put(this);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return CommonResultCode.SUCCESS.getCode().equals(code);
    }

    public static void init() {
        log.info("ResultCode init....");
    }

    // =======================分割=======================

    /**
     * 内部类，用于检测code范围
     */
    @Slf4j
    private static class ResponseCodeContainer {

        private static final Map<Integer, ResultCode> RESPONSE_CODE_MAP = new HashMap<>();

        private static final Map<Class<? extends ResultCode>, int[]> RESPONSE_CODE_RANGE_MAP = new HashMap<>();

        /**
         * id的范围：[start, end]左闭右闭
         *
         * @param clazz
         * @param start
         * @param end
         */
        private static void register(Class<? extends ResultCode> clazz, Integer start,
            Integer end) {
            if (start > end) {
                throw new IllegalArgumentException("<ResultCode> start > end!");
            }

            if (RESPONSE_CODE_RANGE_MAP.containsKey(clazz)) {
                throw new IllegalArgumentException(
                    String.format("<ResultCode> Class:%s already exist!", clazz.getSimpleName()));
            }
            RESPONSE_CODE_RANGE_MAP.forEach((k, v) -> {
                if ((start >= v[0] && start <= v[1]) || (end >= v[0] && end <= v[1])) {
                    throw new IllegalArgumentException(String.format(
                        "<ResultCode> Class:%s 's id range[%d,%d] has " + "intersection with "
                            + "class:%s", clazz.getSimpleName(), start, end,
                        k.getSimpleName()));
                }
            });

            RESPONSE_CODE_RANGE_MAP.put(clazz, new int[]{start, end});

            // 提前初始化static变量，进行范围检测
            Field[] fields = clazz.getFields();
            if (fields.length != 0) {
                try {
                    fields[0].get(clazz);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error("", e);
                }
            }
        }

        private static void put(ResultCode codeConst) {
            int[] idRange = RESPONSE_CODE_RANGE_MAP.get(codeConst.getClass());
            if (idRange == null) {
                throw new IllegalArgumentException(String
                    .format("<ResultCode> Class:%s has not been registered!",
                        codeConst.getClass().getSimpleName()));
            }
            Integer code = codeConst.code;
            if (code < idRange[0] || code > idRange[1]) {
                throw new IllegalArgumentException(String
                    .format("<ResultCode> Id(%d) out of range[%d,%d], " + "class:%s", code,
                        idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
            }
            if (RESPONSE_CODE_MAP.containsKey(code)) {
                log.error(String.format(
                    "<ResultCode> Id(%d) out of range[%d,%d], " + "class:%s  core is repeat!", code,
                    idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
                throw new IllegalArgumentException(String.format(
                    "<ResultCode> Id(%d) out of range[%d,%d], " + "class:%s  core is repeat!", code,
                    idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
            }
            RESPONSE_CODE_MAP.put(code, codeConst);
        }

    }

}
