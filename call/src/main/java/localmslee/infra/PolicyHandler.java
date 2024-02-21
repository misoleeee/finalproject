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
    CallRepository callRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCalled'"
    )
    public void wheneverTaxiCalled_TaxiSaga(@Payload TaxiCalled taxiCalled) {
        TaxiCalled event = taxiCalled;
        System.out.println(
            "\n\n##### listener TaxiSaga : " + taxiCalled + "\n\n"
        );
        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCanceled'"
    )
    public void wheneverTaxiCanceled_CallCanceled(
        @Payload Call call
    ) {
        CallCanceled callCanceled = new CallCanceled(call);
        callCanceled.publishAfterCommit();
    }

    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='TaxiAccepted'"
    // )
    // public void wheneverTaxiAccepted_TaxiSaga(
    //     @Payload TaxiAccepted taxiAccepted
    // ) {
    //     TaxiAccepted event = taxiAccepted;
    //     System.out.println(
    //         "\n\n##### listener TaxiSaga : " + taxiAccepted + "\n\n"
    //     );
    //     // Sample Logic //

    // }

    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='AdvancePayment'"
    // )
    // public void wheneverAdvancePayment_TaxiSaga(
    //     @Payload AdvancePayment advancePayment
    // ) {
    //     AdvancePayment event = advancePayment;
    //     System.out.println(
    //         "\n\n##### listener TaxiSaga : " + advancePayment + "\n\n"
    //     );
    //     // Sample Logic //

    // }

    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='TaxiDepartured'"
    // )
    // public void wheneverTaxiDepartured_TaxiSaga(
    //     @Payload TaxiDepartured taxiDepartured
    // ) {
    //     TaxiDepartured event = taxiDepartured;
    //     System.out.println(
    //         "\n\n##### listener TaxiSaga : " + taxiDepartured + "\n\n"
    //     );
    //     // Sample Logic //

    // }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='FinalPayment'"
    )
    public void wheneverFinalPayment_TaxiSaga(
        @Payload FinalPayment finalPayment
    ) {
        FinalPayment event = finalPayment;
        System.out.println(
            "\n\n##### listener TaxiSaga : " + finalPayment + "\n\n"
        );
        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='CallCompleted'"
    )
    public void wheneverCallCompleted_TaxiSaga(
        @Payload CallCompleted callCompleted
    ) {
        CallCompleted event = callCompleted;
        System.out.println(
            "\n\n##### listener TaxiSaga : " + callCompleted + "\n\n"
        );
        // Sample Logic //

    }

    // @StreamListener(
    //     value = KafkaProcessor.INPUT,
    //     condition = "headers['type']=='TaxiArrived'"
    // )
    // public void wheneverTaxiArrived_TaxiSaga(@Payload TaxiArrived taxiArrived) {
    //     TaxiArrived event = taxiArrived;
    //     System.out.println(
    //         "\n\n##### listener TaxiSaga : " + taxiArrived + "\n\n"
    //     );
    //     // Sample Logic //

    // }
}
//>>> Clean Arch / Inbound Adaptor
