package construction.coatings;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoatingsRepository implements PanacheRepositoryBase<Coatings, String> {
}