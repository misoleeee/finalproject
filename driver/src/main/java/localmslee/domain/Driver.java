package localmslee.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import localmslee.DriverApplication;
import localmslee.domain.TaxiAccepted;
import localmslee.domain.TaxiCanceled;
import localmslee.domain.TaxiDepartured;
import lombok.Data;

@Entity
@Table(name = "Driver_table")
@Data
//<<< DDD / Aggregate Root
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer driverQty;

    private String taxiType;

    @PostPersist
    public void onPostPersist() {
        TaxiAccepted taxiAccepted = new TaxiAccepted(this);
        taxiAccepted.publishAfterCommit();

        TaxiDepartured taxiDepartured = new TaxiDepartured(this);
        taxiDepartured.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        TaxiCanceled taxiCanceled = new TaxiCanceled(this);
        taxiCanceled.publishAfterCommit();
    }

    public static DriverRepository repository() {
        DriverRepository driverRepository = DriverApplication.applicationContext.getBean(
            DriverRepository.class
        );
        return driverRepository;
    }
}
//>>> DDD / Aggregate Root
