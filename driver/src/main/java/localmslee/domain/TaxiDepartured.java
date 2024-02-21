package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class TaxiDepartured extends AbstractEvent {

    private Long id;
    private Integer driverQty;
    private String taxiType;

    public TaxiDepartured(Driver aggregate) {
        super(aggregate);
    }

    public TaxiDepartured() {
        super();
    }
}
//>>> DDD / Domain Event
