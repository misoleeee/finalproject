package localmslee.infra;

import localmslee.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class DriverHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Driver>> {

    @Override
    public EntityModel<Driver> process(EntityModel<Driver> model) {
        return model;
    }
}
