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
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiAccepted'"
    )
    public void wheneverTaxiAccepted_AdvancePayment(
        @Payload Payment payment
    ) {
        AdvancePayment advancePayment = new AdvancePayment(payment);
        advancePayment.publishAfterCommit();
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='FinalPayment'"
    )
    public void wheneverAdvancePayment_FinalPayment(
        @Payload Payment payment
    ) {
        FinalPayment finalPayment = new FinalPayment(payment);
        finalPayment.publishAfterCommit();
    }
}
//>>> Clean Arch / Inbound Adaptor
