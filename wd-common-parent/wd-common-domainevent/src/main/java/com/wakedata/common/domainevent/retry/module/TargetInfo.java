package com.wakedata.common.domainevent.retry.module;

import lombok.Data;

/**
 * 目标类class信息
 * @author hhf
 * @date 2022/6/14
 */
@Data
public class TargetInfo {
    String className;
    String methodName;
    String paramClassName;
}