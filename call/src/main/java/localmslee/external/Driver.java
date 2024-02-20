package localmslee.external;

import java.util.Date;
import lombok.Data;

@Data
public class Driver {

    private Long id;
    private Long driverId;
    private String status;
    private Date callDt;
    private Integer driverQty;
    private Long customerId;
    private Integer charge;
    private String paymentStatus;
}
