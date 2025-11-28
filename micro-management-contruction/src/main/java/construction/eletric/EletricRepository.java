package construction.eletric;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EletricRepository implements PanacheRepositoryBase<Eletric, String> {
}