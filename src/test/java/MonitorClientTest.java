import com.alibaba.fastjson.JSON;
import com.lsyf.pay.logger.service.MonitorClient;
import com.lsyf.pay.logger.service.res.Monitor1Res;
import com.lsyf.pay.logger.service.res.Monitor2Res;
import com.lsyf.pay.logger.service.res.UserIdType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MonitorClientTest extends AppTest {
    @Autowired
    MonitorClient client;

    @Test
    public void test1() {
        List<Monitor1Res> list = client.monitor1("201910", 1, UserIdType.cpf);
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    public void test2() {
        List<Monitor2Res> list = client.monitor2("20191001", 1.0, UserIdType.cpf);
        System.out.println(JSON.toJSONString(list));
    }
}
