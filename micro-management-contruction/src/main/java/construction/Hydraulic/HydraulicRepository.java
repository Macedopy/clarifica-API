package construction.hydraulic;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HydraulicRepository implements PanacheRepositoryBase<Hydraulic, String> {
}