package ru.ecofin.service.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findUserByPhone(String phone);
}
