package s4.spring.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import s4.spring.td5.entities.Script;
import s4.spring.td5.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer>
{
    List<User> findAll();
    User findById(int Id);
    User findByLogin(String login);
}
