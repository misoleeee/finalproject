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
    private String customerId;
    private Integer charge;

    public CallCompleted(Call aggregate) {
        super(aggregate);
    }

    public CallCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
