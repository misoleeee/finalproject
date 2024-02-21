package localmslee.domain;

import java.time.LocalDate;
import java.util.*;
import localmslee.infra.AbstractEvent;
import lombok.Data;

@Data
public class TaxiAccepted extends AbstractEvent {

    private Long id;
    private Integer DriverQty;
    private String taxiType;
}
