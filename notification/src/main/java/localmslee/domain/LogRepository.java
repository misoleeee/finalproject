package localmslee.domain;

import localmslee.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "logs", path = "logs")
public interface LogRepository extends PagingAndSortingRepository<Log, Long> {}
