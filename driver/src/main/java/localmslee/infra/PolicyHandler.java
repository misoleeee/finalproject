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
        // driverQty Decrease
        Integer qty = driverRepository.findById(Long.parseLong(driver.getTaxiType())).get().getDriverQty();

        if(qty > 0 ){
            //kafka message 처리 (TaxiAccepted 호출)
            driverRepository.findById(Long.parseLong(driver.getTaxiType())).ifPresent(inventory->{
                driver.setDriverQty(qty-1);
                driverRepository.save(driver);
            });

            TaxiAccepted taxiAccepted = new TaxiAccepted(driver);
            taxiAccepted.publishAfterCommit();
        }
        else {
            TaxiCanceled taxiCanceled = new TaxiCanceled(driver);
            taxiCanceled.publishAfterCommit();
        }
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCancelled'"
    )
    public void wheneverTaxiCancelled_CancelCall(
        @Payload Driver driver
    ) {
        // driverQty Increase
        Integer qty = driverRepository.findById(Long.parseLong(driver.getTaxiType())).get().getDriverQty();
        //kafka message 처리
        driverRepository.findById(Long.parseLong(driver.getTaxiType())).ifPresent(inventory->{
            driver.setDriverQty(qty+1);
            driverRepository.save(driver);
        });
        TaxiCanceled taxiCanceled = new TaxiCanceled(driver);
        taxiCanceled.publishAfterCommit();
    }


    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AdvancePayment'"
    )
    public void wheneverAdvancePayment_TaxiDepartured(
        @Payload Driver driver
    ) {
        TaxiDepartured taxiDepartured = new TaxiDepartured(driver);
        taxiDepartured.publishAfterCommit();
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiDepartured'"
    )
    public void wheneverTaxiDepartured_TaxiArrived(
        @Payload Driver driver
    ) {
        TaxiArrived taxiArrived = new TaxiArrived(driver);
        taxiArrived.publishAfterCommit();
    }
}
//>>> Clean Arch / Inbound Adaptor
