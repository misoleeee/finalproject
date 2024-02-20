package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CallCompleted extends AbstractEvent {

    private Long id;
    private String status;
    private Date callDt;
    private Integer charge;
    private Long customerId;

    public CallCompleted(Call aggregate) {
        super(aggregate);
    }

    public CallCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
