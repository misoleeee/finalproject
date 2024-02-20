package localmslee.infra;

import localmslee.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class LogHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Log>> {

    @Override
    public EntityModel<Log> process(EntityModel<Log> model) {
        return model;
    }
}
