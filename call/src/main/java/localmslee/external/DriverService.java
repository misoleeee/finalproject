package localmslee.external;

import java.util.Date;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "driver", url = "${api.url.driver}")
public interface DriverService {
    @RequestMapping(method = RequestMethod.POST, path = "/drivers")
    public void taxiCall(@RequestBody Driver driver);

    @RequestMapping(method = RequestMethod.POST, path = "/drivers")
    public void taxiDeparture(@RequestBody Driver driver);

    @RequestMapping(method = RequestMethod.DELETE, path = "/drivers")
    public void cancelCall(@RequestBody Driver driver);

    @RequestMapping(method = RequestMethod.POST, path = "/drivers")
    public void arriveTaxi(@RequestBody Driver driver);
}
