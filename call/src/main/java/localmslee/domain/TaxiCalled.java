package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class TaxiCalled extends AbstractEvent {

    private Long id;
    private Long customerId;
    private String status;
    private Date callDt;
    private Integer charge;
    private String taxiType;

    public TaxiCalled(Call aggregate) {
        super(aggregate);
    }

    public TaxiCalled() {
        super();
    }
}
//>>> DDD / Domain Event
