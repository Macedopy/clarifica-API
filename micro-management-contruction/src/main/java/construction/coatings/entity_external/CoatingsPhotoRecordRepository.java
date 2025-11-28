package construction.coatings.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoatingsPhotoRecordRepository implements PanacheRepository<CoatingsPhotoRecord> {
}