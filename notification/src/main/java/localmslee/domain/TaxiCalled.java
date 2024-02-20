package localmslee.domain;

import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class TaxiCalled extends AbstractEvent {

    private Long id;
    private String status;
    private Date callDt;
    private String customerId;
    private Integer charge;
}
