package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class TaxiArrived extends AbstractEvent {

    private Long id;

    public TaxiArrived(Driver aggregate) {
        super(aggregate);
    }

    public TaxiArrived() {
        super();
    }
}
//>>> DDD / Domain Event
