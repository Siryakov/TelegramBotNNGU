package Siryakov.com;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Здесь можно добавить дополнительные методы для работы с базой данных
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findByGroupNumber(String groupNumber);
    void deleteById(Long id);
    <S extends User> S save(S entity); // void save(User user)
    }