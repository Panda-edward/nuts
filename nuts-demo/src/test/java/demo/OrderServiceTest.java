package demo;

import com.ed.component.nuts.repository.INutsRepository;
import com.ed.component.nuts.repository.NutsRetryRecord;
import com.ed.demo.nuts.DemoApplication;
import com.ed.demo.nuts.OrderService;
import com.ed.demo.nuts.vo.AddOrderParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:16
 */
@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Resource
    OrderService orderService;

    @Resource
    private INutsRepository<NutsRetryRecord> nutsRepository;


    @Test
    public void test() throws Exception {
//        AddOrderParam param = new AddOrderParam().setOrderId(2223348L).setPayTime(new Date());
//        orderService.createOrder(param);


        List<NutsRetryRecord> records = nutsRepository.queryRetries(10);
        System.out.println(records);

        Thread.sleep(12 * 1000);

    }

}
