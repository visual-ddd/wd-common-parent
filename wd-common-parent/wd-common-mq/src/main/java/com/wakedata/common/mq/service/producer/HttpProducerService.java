package com.wakedata.common.mq.service.producer;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.exception.RequestResultException;
import com.wakedata.common.mq.exception.HttpRequestException;
import com.wakedata.common.mq.exception.SendMessageException;
import com.wakedata.common.mq.model.producer.MakeMessageBody;

/**
 * Http生产者服务
 *
 * @author chenshaopeng
 * @date 2021/11/15
 */
public class HttpProducerService extends AbstractProducerService{

    private static final String USER_AGENT = "event_center";

    public static final String SUCCESS_TAG = "success";


    @Override
    public Boolean sendMessage(MakeMessageBody info) {
        try {
            HttpResponse result = HttpRequest.post(info.getBootstrapServers())
                    .header(Header.USER_AGENT, USER_AGENT)
                    .timeout(getRequestTimeout(info.getRequestTimeout()))
                    .body(info.getMessage())
                    .execute(false);
            if(HttpStatus.HTTP_OK == result.getStatus()){
                if(SUCCESS_TAG.equalsIgnoreCase(result.body())){
                    return true;
                }
                throw new RequestResultException("Incorrect reply core: " + result.body());
            }
            throw new HttpRequestException(result.getStatus() + ": " + result.body());
        } catch (IORuntimeException e) {
            throw new ConnectException(e);
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

}
