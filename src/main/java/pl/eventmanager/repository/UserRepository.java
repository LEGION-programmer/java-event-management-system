package pl.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.eventmanager.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}