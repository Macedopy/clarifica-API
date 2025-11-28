package construction.eletric.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EletricMachineryRepository implements PanacheRepository<EletricMachinery> {
}