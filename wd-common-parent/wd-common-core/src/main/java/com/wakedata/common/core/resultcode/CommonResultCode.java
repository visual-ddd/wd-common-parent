package com.wakedata.common.core.resultcode;

/**
 * @author: hhf
 * @date: 2021/1/29
 **/
public class CommonResultCode extends ResultCode {

    public static final CommonResultCode SUCCESS = new CommonResultCode(100, "操作成功!");

    public static final CommonResultCode ERROR_PARAM = new CommonResultCode(101, "common.param.error");

    public static final CommonResultCode ERROR_PARAM_ANY = new CommonResultCode(102, "%s参数错误！");

    public static final CommonResultCode ILLEGAL_PARAM = new CommonResultCode(103, "common.illegal.error");

    public static final CommonResultCode EMPTY_PARAM = new CommonResultCode(104, "参数不能为空");

    public static final CommonResultCode SYSTEM_ERROR = new CommonResultCode(105, "common.system.error");

    public static final CommonResultCode BUSINESS_ERROR = new CommonResultCode(106, "业务异常，请稍后再试");

    public static final CommonResultCode DEVELOPMENT = new CommonResultCode(107, "此功能正在开发中");

    public static final CommonResultCode NOT_EXISTS = new CommonResultCode(108, "数据不存在");

    public static final CommonResultCode REQUEST_METHOD_ERROR = new CommonResultCode(109, "common.method.error");

    public static final CommonResultCode BUSINESS_DIALOG_ERROR = new CommonResultCode(110, "业务弹框异常，msg自己定义");

    public static final CommonResultCode LOGIN_ERROR = new CommonResultCode(401, "您还未登录或登录失效，请重新登录！");

    public static final CommonResultCode S_ERROR = new CommonResultCode(1001, "系统繁忙，请稍后再试");

    public static final CommonResultCode S_SERVICE_PAUSE = new CommonResultCode(1002, "服务暂停");

    public static final CommonResultCode S_SERVICE_BUSY = new CommonResultCode(1003, "服务器忙");

    public static final CommonResultCode S_INTERFACE_ERROR = new CommonResultCode(1004, "接口不存在");

    public static final CommonResultCode S_INTERFACE_TIMEOUT = new CommonResultCode(1005, "接口超时");

    public static final CommonResultCode S_ERR_CHECK_TEXT = new CommonResultCode(1006, "文本不合法");

    public static final CommonResultCode S_ERR_INVALID_REQUEST = new CommonResultCode(1007, "请求无效");

    public static final CommonResultCode S_AUTH_INFO_EMPTY = new CommonResultCode(1008, "未获取到当前用户相关授权信息");

    public static final CommonResultCode S_RATE_LIMIT = new CommonResultCode(1009, "请求过于频繁，请稍后重试");

    public static final CommonResultCode S_DEGRADE = new CommonResultCode(1010, "系统降级中，请稍后重试");

    public static final CommonResultCode S_BLOCK = new CommonResultCode(1011, "系统保护中，请稍后重试");

    public static final CommonResultCode S_GET_LOCK_FAIL = new CommonResultCode(1012, "获取同步锁失败");

    /**
     * jwt 无效
     */
    public static final CommonResultCode JWT_INVALID = new CommonResultCode(1110, "common.jwt.invalid");

    /**
     * jwt 已过期或未生效
     */
    public static final CommonResultCode JWT_EXPIRED = new CommonResultCode(1111, "common.jwt.expired");

    public CommonResultCode(Integer code, String msg) {
        super(code, msg);
    }
}
