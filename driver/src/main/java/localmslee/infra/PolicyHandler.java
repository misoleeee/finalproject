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
    public void TaxiCalled_CallAccepted(
        @Payload Driver driver
    ) {
        Driver event = driver;
        System.out.println("\n\n==================================================");
        System.out.println("##### listener IncreaseStock : " + driver + " / EventInfo : " + event + "\n\n");
        
        // Driver temp2 = driverRepository.findById(driver.getDriverId()).get();
        // temp2.setDriverQty(temp2.getDriverQty() - 1);
        // driver.setDriverQty(driverRepository.findById(driver.getId()) - 1);

        System.out.println("\n\n==================================================");
        System.out.println("\n\n==================================================");
        System.out.println(Long.valueOf(event.getCustomerId()));
        System.out.println(driver.getDriverQty());
        System.out.println(driverRepository.findById(Long.valueOf(driver.getId())));
        System.out.println("\n\n==================================================");
        System.out.println("\n\n==================================================");
        
        driverRepository.findById(Long.valueOf(event.getCustomerId())).ifPresent(driver->{
            driver.setDriverQty(driver.getDriverQty() + 1); 
            driverRepository.save(driver);
        });
        
        // System.out.println("##### listener 줄어든 driver 수량 : " + temp2);
        // driverRepository.save(temp2);
        // System.out.println("\n\n==================================================");
        // System.out.println("##### save Repository Information : " + driverRepository.findById(driver.getId()));

        // Driver.TaxiCall(event);

        // driver.setPrdStatus("결제완료");
        CallAccepted callAccepted = new CallAccepted(driver);
        callAccepted.publishAfterCommit();
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
