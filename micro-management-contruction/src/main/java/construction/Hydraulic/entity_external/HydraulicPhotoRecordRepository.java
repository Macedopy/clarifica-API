package construction.hydraulic.entity_external;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HydraulicPhotoRecordRepository implements PanacheRepository<HydraulicPhotoRecord> {
}
