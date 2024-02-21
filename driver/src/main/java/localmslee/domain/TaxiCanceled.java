package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class TaxiCanceled extends AbstractEvent {

    private Long id;
    private Integer driverQty;
    private String taxiType;
    private String status;

    public TaxiCanceled(Driver aggregate) {
        super(aggregate);
    }

    public TaxiCanceled() {
        super();
    }
}
//>>> DDD / Domain Event
