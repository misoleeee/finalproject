package localmslee.domain;

import java.util.*;
import localmslee.domain.*;
import localmslee.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class TaxiDepartured extends AbstractEvent {

    private Long id;
    private Integer driverQty;
    private Long driverId;
}
