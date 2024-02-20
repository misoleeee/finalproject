package localmslee.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import localmslee.DriverApplication;
import localmslee.domain.CallAccepted;
import localmslee.domain.CallCanceled;
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

    private Long driverId;

    private String status;

    private Date callDt;

    private Integer driverQty;

    private Long customerId;

    private Integer charge;

    private String paymentStatus;

    @PostPersist
    public void onPostPersist() {
        CallAccepted callAccepted = new CallAccepted(this);
        callAccepted.publishAfterCommit();

        TaxiDepartured taxiDepartured = new TaxiDepartured(this);
        taxiDepartured.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        CallCanceled callCanceled = new CallCanceled(this);
        callCanceled.publishAfterCommit();
    }

    public static DriverRepository repository() {
        DriverRepository driverRepository = DriverApplication.applicationContext.getBean(
            DriverRepository.class
        );
        return driverRepository;
    }

    public static void TaxiCall(Driver event){
        repository().findById(Long.valueOf(event.getId())).ifPresent(driver->{
            driver.setDriverQty(driver.getDriverQty() - 1); 
            repository().save(driver);
        });
    }

    public static void CancelCall(Driver event){
        repository().findById(Long.valueOf(event.getId())).ifPresent(driver->{
            driver.setDriverQty(driver.getDriverQty() + 1); 
            repository().save(driver);
        });
    }
}
//>>> DDD / Aggregate Root
