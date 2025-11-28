package construction.finishing;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FinishingRepository implements PanacheRepositoryBase<Finishing, String> {
}