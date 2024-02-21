package localmslee.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import localmslee.NotificationApplication;
import lombok.Data;

@Entity
@Table(name = "Log_table")
@Data
//<<< DDD / Aggregate Root
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customerId;

    public static LogRepository repository() {
        LogRepository logRepository = NotificationApplication.applicationContext.getBean(
            LogRepository.class
        );
        return logRepository;
    }

    //<<< Clean Arch / Port Method
    public static void alertToUser(TaxiCalled taxiCalled) {
        //implement business logic here:

        /** Example 1:  new item 
        Log log = new Log();
        repository().save(log);

        */

        /** Example 2:  finding and process
        
        repository().findById(taxiCalled.get???()).ifPresent(log->{
            
            log // do something
            repository().save(log);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertToUser(AdvancePayment advancePayment) {
        //implement business logic here:

        /** Example 1:  new item 
        Log log = new Log();
        repository().save(log);

        */

        /** Example 2:  finding and process
        
        repository().findById(advancePayment.get???()).ifPresent(log->{
            
            log // do something
            repository().save(log);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertToUser(FinalPayment finalPayment) {
        //implement business logic here:

        /** Example 1:  new item 
        Log log = new Log();
        repository().save(log);

        */

        /** Example 2:  finding and process
        
        repository().findById(finalPayment.get???()).ifPresent(log->{
            
            log // do something
            repository().save(log);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertToUser(TaxiAccepted taxiAccepted) {
        //implement business logic here:

        /** Example 1:  new item 
        Log log = new Log();
        repository().save(log);

        */

        /** Example 2:  finding and process
        
        repository().findById(taxiAccepted.get???()).ifPresent(log->{
            
            log // do something
            repository().save(log);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void alertToUser(TaxiDepartured taxiDepartured) {
        //implement business logic here:

        /** Example 1:  new item 
        Log log = new Log();
        repository().save(log);

        */

        /** Example 2:  finding and process
        
        repository().findById(taxiDepartured.get???()).ifPresent(log->{
            
            log // do something
            repository().save(log);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
