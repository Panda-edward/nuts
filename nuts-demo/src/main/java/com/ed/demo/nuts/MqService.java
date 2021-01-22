package com.ed.demo.nuts;

import com.ed.component.nuts.RetryStrategy;
import com.ed.component.nuts.anno.Nuts;
import com.ed.component.nuts.anno.NutsBizNo;
import com.ed.component.nuts.anno.NutsRetry;
import com.ed.demo.nuts.vo.OrderStatusMsgVO;
import org.springframework.stereotype.Service;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:08
 */
@Service
public class MqService {

    @Nuts(name = "sendMsg", nutsRetry = @NutsRetry(interval = 30, strategy = RetryStrategy.FIXED))
    public void sendMsg(@NutsBizNo Long bizRequestId, OrderStatusMsgVO msgVO) {
        System.out.println("发送消息:" + msgVO);
    }
}
