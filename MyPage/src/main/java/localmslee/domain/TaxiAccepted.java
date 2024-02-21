package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.infra.AbstractEvent;
import lombok.Data;

@Data
public class TaxiAccepted extends AbstractEvent {

    private Long id;
    private Long driverId;
    private String Status;
    private Date CallDt;
    private Integer DriverQty;
    private Long customerId;
    private Integer Charge;
    private String PaymentStatus;
}
