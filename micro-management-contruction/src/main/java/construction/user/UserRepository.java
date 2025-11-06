package construction.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
    public List<User> findAllCustomers() {
        return listAll();
    }
    
    public User findByIdOptional(String id) {
        return find("id", id).firstResult();
    }
}