package com.ed.demo.nuts.vo;

import com.ed.component.nuts.NutsVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : Edward
 * @date : 2020/11/3 下午4:27
 */
@Data
@Accessors(chain = true)
public class OrderStatusMsgVO implements NutsVO {

    private Long orderId;

    private Integer status;

    /**
     * 获取业务主键
     *
     * @return
     */
    @Override
    public String getBizNo() {
        return orderId + "_" + status;
    }
}
