package localmslee.infra;

import java.util.List;
import localmslee.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "myPages", path = "myPages")
public interface MyPageRepository
    extends PagingAndSortingRepository<MyPage, Long> {}

    
        // driverRepository.findById(Long.valueOf(event.getCustomerId())).ifPresent(driver->{
        //     driver.setDriverQty(driver.getDriverQty() + 1); 
        //     driverRepository.save(driver);
        // });
        
        // System.out.println("##### listener 줄어든 driver 수량 : " + temp2);
        // driverRepository.save(temp2);
        // System.out.println("\n\n==================================================");
        // System.out.println("##### save Repository Information : " + driverRepository.findById(driver.getId()));
