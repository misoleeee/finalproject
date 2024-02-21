package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AdvancePayment extends AbstractEvent {

    private Long id;
    private Long customerId;
    private Long driverId;
    private Date callDt;
    private Integer charge;
    private String paymentStatus;

    public AdvancePayment(Payment aggregate) {
        super(aggregate);
    }

    public AdvancePayment() {
        super();
    }
}
//>>> DDD / Domain Event
