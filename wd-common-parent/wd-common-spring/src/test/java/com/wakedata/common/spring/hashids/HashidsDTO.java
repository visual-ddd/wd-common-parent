package com.wakedata.common.spring.hashids;

import com.wakedata.common.core.hashids.HashidsConstant;
import com.wakedata.common.core.hashids.annotation.HashidsConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author luomeng
 * @Description id和hash相互转换
 * @createTime 2022-04-18 19:40:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashidsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @HashidsConvert
    private Long id;
    @HashidsConvert(salt = "long123456",minHashLength = 20)
    private long idLong;
    @HashidsConvert(salt = "int123456",minHashLength = 8)
    private int idInt;
    @HashidsConvert(salt = "integer123456",minHashLength = 9)
    private Integer idInteger;
    @HashidsConvert(salt = HashidsConstant.DEFAULT_SALT_PREFIX,minHashLength = 10)
    private String idStr;
    private Long idNoConvert;

}
