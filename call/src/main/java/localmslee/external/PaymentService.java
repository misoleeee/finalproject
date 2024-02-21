package localmslee.external;

import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "payment", url = "${api.url.payment}")
public interface PaymentService {
    @RequestMapping(method = RequestMethod.POST, path = "/payments")
    public void advancePayment(@RequestBody Payment payment);

    @RequestMapping(method = RequestMethod.PATCH, path = "/payments")
    public void finalPayment(@RequestBody Payment payment);
}
