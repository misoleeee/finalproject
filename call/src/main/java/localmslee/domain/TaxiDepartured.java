package localmslee.domain;

import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class TaxiDepartured extends AbstractEvent {

    private Long id;
    private Long driverId;
    private String status;
    private Date callDt;
    private Integer driverQty;
    private Long customerId;
    private Integer charge;
    private String paymentStatus;
}
