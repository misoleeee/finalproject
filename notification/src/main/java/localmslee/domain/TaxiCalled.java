package localmslee.domain;

import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class TaxiCalled extends AbstractEvent {

    private Long id;
    private Long customerId;
    private String status;
    private Date callDt;
    private Integer charge;
    private String taxiType;
}
