package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class TaxiAccepted extends AbstractEvent {

    private Long id;
    private Integer driverQty;
    private Long driverId;

    public TaxiAccepted(Driver aggregate) {
        super(aggregate);
    }

    public TaxiAccepted() {
        super();
    }
}
//>>> DDD / Domain Event
