package wdefassio.io.statlessauthapi.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wdefassio.io.statlessauthapi.code.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
