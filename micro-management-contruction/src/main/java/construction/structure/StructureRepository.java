package construction.structure;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StructureRepository implements PanacheRepositoryBase<Structure, String> {
}