package demo;

import com.ed.demo.nuts.DemoApplication;
import com.ed.demo.nuts.OrderService;
import com.ed.demo.nuts.vo.AddOrderParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:16
 */
@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Resource
    OrderService orderService;


    @Test
    public void test() throws Exception {
        AddOrderParam param = new AddOrderParam().setOrderId("D283273").setPayTime(new Date());
        orderService.createOrder(param);
    }

}
