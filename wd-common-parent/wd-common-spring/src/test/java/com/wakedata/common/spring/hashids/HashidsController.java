package com.wakedata.common.spring.hashids;

import cn.hutool.json.JSONUtil;
import com.wakedata.common.core.dto.ResultDTO;
import com.wakedata.common.core.hashids.annotation.HashidsConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * @author luomeng
 * @Description id转hash
 * @createTime 2022-04-18 19:34:00
 */
@RestController
@RequestMapping("/heartbeat")
@Slf4j
public class HashidsController {

    /**
     * id convert hash
     * @param hashidsDTO
     * @return
     */
    @GetMapping("/convert.ids")
    public ResultDTO<HashidsDTO> convertIds(@RequestBody HashidsDTO hashidsDTO){
        //测试信息输出
        log.info("JSONUtil.toJsonPrettyStr(hashidsDTO) = " + JSONUtil.toJsonPrettyStr(hashidsDTO));
        HashidsDTO dto = new HashidsDTO();
        dto.setId(100L);
        dto.setIdNoConvert(100L);
        dto.setIdLong(100L);
        dto.setIdInt(100);
        dto.setIdInteger(100);
        dto.setIdStr("100");
        log.info("JSONUtil.toJsonPrettyStr(dto) = " + JSONUtil.toJsonPrettyStr(dto));
        return ResultDTO.success(dto);
    }


    /**
     * id参数转换，注解应用在参数上
     * @param id
     * @return
     */
    @GetMapping("/convert.test.param")
    public ResultDTO<String> converIdsParam(@HashidsConvert Long id,@HashidsConvert(salt = "long123456",minHashLength = 20) Long hashid){
        String result = "id：%s，hashid：%s";
        return ResultDTO.success(String.format(result,id,hashid));
    }


}
