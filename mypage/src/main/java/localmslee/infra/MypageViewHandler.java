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
public class MypageViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private MypageRepository mypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiCalled_then_CREATE_1(@Payload TaxiCalled taxiCalled) {
        try {
            if (!taxiCalled.validate()) return;

            // view 객체 생성
            Mypage mypage = new Mypage();
            // view 객체에 이벤트의 Value 를 set 함
            mypage.setId(taxiCalled.getId());
            mypage.setCustomerId(Long.valueOf(taxiCalled.getCustomerId()));
            mypage.setStatus(taxiCalled.getStatus());
            mypage.setCallDt(taxiCalled.getCallDt());
            mypage.setCharge(taxiCalled.getCharge());
            // view 레파지 토리에 save
            mypageRepository.save(mypage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenTaxiAccepted_then_UPDATE_1(
        @Payload TaxiAccepted taxiAccepted
    ) {
        try {
            if (!taxiAccepted.validate()) return;
            // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findById(
                taxiAccepted.getId()
            );

            if (mypageOptional.isPresent()) {
                Mypage mypage = mypageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setStatus(taxiAccepted.getStatus());
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCallCompleted_then_UPDATE_2(
        @Payload CallCompleted callCompleted
    ) {
        try {
            if (!callCompleted.validate()) return;
            // view 객체 조회
            Optional<Mypage> mypageOptional = mypageRepository.findById(
                callCompleted.getId()
            );

            if (mypageOptional.isPresent()) {
                Mypage mypage = mypageOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setStatus(callCompleted.getStatus());
                // view 레파지 토리에 save
                mypageRepository.save(mypage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
