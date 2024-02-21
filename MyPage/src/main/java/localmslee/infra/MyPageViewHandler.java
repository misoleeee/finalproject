package localmslee.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import localmslee.config.kafka.KafkaProcessor;
import localmslee.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MyPageViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private MyPageRepository myPageRepository;

    // @StreamListener(KafkaProcessor.INPUT)
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='TaxiCalled'"
    )
    public void whenTaxiCalled_then_CREATE_1(@Payload TaxiCalled taxiCalled) {
        try {
            if (!taxiCalled.validate()) return;

            // view 객체 생성
            MyPage myPage = new MyPage();
            // view 객체에 이벤트의 Value 를 set 함
            myPage.setId(taxiCalled.getId());
            myPage.setCustomerId(Long.valueOf(taxiCalled.getCustomerId()));
            myPage.setStatus(taxiCalled.getStatus());
            myPage.setCallDt(taxiCalled.getCallDt());
            myPage.setCharge(taxiCalled.getCharge());
            // view 레파지 토리에 save
            myPageRepository.save(myPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @StreamListener(KafkaProcessor.INPUT)
    // public void whenTaxiAccepted_then_UPDATE_1(
    //     @Payload TaxiAccepted taxiAccepted
    // ) {
    //     try {
    //         if (!taxiAccepted.validate()) return;
    //         // view 객체 조회
    //         Optional<MyPage> myPageOptional = myPageRepository.findById(
    //             taxiAccepted.getId()
    //         );

    //         if (myPageOptional.isPresent()) {
    //             MyPage myPage = myPageOptional.get();
    //             // view 객체에 이벤트의 eventDirectValue 를 set 함
    //             myPage.setStatus(taxiAccepted.get);
    //             // view 레파지 토리에 save
    //             myPageRepository.save(myPage);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCallCompleted_then_UPDATE_2(
        @Payload CallCompleted callCompleted
    ) {
        try {
            if (!callCompleted.validate()) return;
            // view 객체 조회
            Optional<MyPage> myPageOptional = myPageRepository.findById(
                callCompleted.getId()
            );

            if (myPageOptional.isPresent()) {
                MyPage myPage = myPageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus(callCompleted.getStatus());
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
