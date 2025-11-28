package construction.finishing.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FinishingToolRepository implements PanacheRepository<FinishingTool> {
}