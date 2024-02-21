package localmslee.external;

import java.util.Date;
import lombok.Data;

@Data
public class Payment {

    private Long id;
    private Long customerId;
    private Long driverId;
    private Date callDt;
    private Integer charge;
    private String paymentStatus;
}
