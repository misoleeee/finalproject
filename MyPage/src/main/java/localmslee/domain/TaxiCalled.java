package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.infra.AbstractEvent;
import lombok.Data;

@Data
public class TaxiCalled extends AbstractEvent {

    private Long id;
    private String Status;
    private Date CallDt;
    private Integer Charge;
    private Long customerId;
}
