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
    LogRepository logRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCalled'"
    )
    public void wheneverTaxiCalled_AlertToUser(@Payload TaxiCalled taxiCalled) {
        TaxiCalled event = taxiCalled;
        System.out.println(
            "\n\n##### listener AlertToUser : " + taxiCalled + "\n\n"
        );

        // Sample Logic //
        Log.alertToUser(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AdvancePayment'"
    )
    public void wheneverAdvancePayment_AlertToUser(
        @Payload AdvancePayment advancePayment
    ) {
        AdvancePayment event = advancePayment;
        System.out.println(
            "\n\n##### listener AlertToUser : " + advancePayment + "\n\n"
        );

        // Sample Logic //
        Log.alertToUser(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='FinalPayment'"
    )
    public void wheneverFinalPayment_AlertToUser(
        @Payload FinalPayment finalPayment
    ) {
        FinalPayment event = finalPayment;
        System.out.println(
            "\n\n##### listener AlertToUser : " + finalPayment + "\n\n"
        );

        // Sample Logic //
        Log.alertToUser(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CallAccepted'"
    )
    public void wheneverCallAccepted_AlertToUser(
        @Payload CallAccepted callAccepted
    ) {
        CallAccepted event = callAccepted;
        System.out.println(
            "\n\n##### listener AlertToUser : " + callAccepted + "\n\n"
        );

        // Sample Logic //
        Log.alertToUser(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiDepartured'"
    )
    public void wheneverTaxiDepartured_AlertToUser(
        @Payload TaxiDepartured taxiDepartured
    ) {
        TaxiDepartured event = taxiDepartured;
        System.out.println(
            "\n\n##### listener AlertToUser : " + taxiDepartured + "\n\n"
        );

        // Sample Logic //
        Log.alertToUser(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
