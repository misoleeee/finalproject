package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.infra.AbstractEvent;
import lombok.Data;

@Data
public class CallCompleted extends AbstractEvent {

    private Long id;
    private Long customerId;
    private String status;
    private Date callDt;
    private Integer charge;
    private String taxiType;
}
