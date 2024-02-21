package localmslee.domain;

import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class FinalPayment extends AbstractEvent {

    private Long id;
    private Long customerId;
    private Long driverId;
    private Date callDt;
    private Integer charge;
    private String paymentStatus;
}
