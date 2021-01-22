package com.ed.demo.nuts.vo;

import com.ed.component.nuts.NutsVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:12
 */
@Data
@Accessors(chain = true)
public class AddOrderParam implements NutsVO {

    private String orderId;

    private Date payTime;

    /**
     * 获取业务主键
     *
     * @return
     */
    @Override
    public String getBizNo() {
        return orderId;
    }
}
