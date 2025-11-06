package construction.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Transactional
    public List<UserDTO> allCustomers() {
        return userRepository.findAllCustomers()
        .stream()
        .map(this::mapToDTO)
        .collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserDTO> customerById(String id) {
        User user = repository.findByIdOptional(id);
        return user != null ? Optional.of(mapToDTO(user)) : Optional.empty();
    }

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
            user.getId(),
            new UserDTO.PII(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCpf()
            )
        );
    }
}
