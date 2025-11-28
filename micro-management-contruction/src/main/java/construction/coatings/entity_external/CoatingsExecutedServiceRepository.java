package construction.coatings.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoatingsExecutedServiceRepository implements PanacheRepository<CoatingsExecutedService> {
}