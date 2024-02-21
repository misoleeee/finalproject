package localmslee.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import localmslee.config.kafka.KafkaProcessor;
import localmslee.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    DriverRepository driverRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCalled'"
    )
    public void TaxiCalled_TaxiAccepted(
        @Payload Driver driver
    ) {
        Driver event = driver;

        // driverQty Decrease
        System.out.println("==================================================");

        Integer temp = 1;
        // System.out.println("##### driverQty Before Decrease : " + driverRepository.findByDriverId(Long.valueOf(temp)) + "");

        Integer qty = driverRepository.findByDriverId(Long.valueOf(temp)).get().getDriverQty();

        System.out.println("##### driverQty Before Decrease : " + qty + "");


        if(qty > 0 ){
            //kafka message 처리 (TaxiAccepted 호출)
            TaxiAccepted TaxiAccepted = new TaxiAccepted(driver);
            TaxiAccepted.publishAfterCommit();
            
        }
        else {
            
            TaxiCanceled taxiCanceled = new TaxiCanceled(driver);
            taxiCanceled.publishAfterCommit();
        

        }

        // Integer driverQty = driverRepository.findByDriverId(Long.valueOf(temp)).get().getDriverQty()-1;
        // driver.setDriverQty(driverQty);
        // driverRepository.save(driver);

        // System.out.println("##### driverQty After Decrease : " + driverRepository.findByDriverId(Long.valueOf(temp)) + "\n\n");
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCancelled'"
    )
    public void wheneverTaxiCancelled_CancelCall(
        @Payload Driver driver
    ) {
        Driver event = driver;
        System.out.println(
            "\n\n##### listener IncreaseStock : " + driver + "\n\n"
        );

        driver.setDriverQty(driver.getDriverQty() + 1);
        driverRepository.save(driver);
        // Driver.CancelCall(event);

        // repository().findById(Long.valueOf(event.getId())).ifPresent(driver->{
        //     driver.setDriverQty(driver.getDriverQty() + event.getDriverQty()); 
        //     driverRepository.save(driver);

        // });

        // Sample Logic //
        // Inventory.increaseStock(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
