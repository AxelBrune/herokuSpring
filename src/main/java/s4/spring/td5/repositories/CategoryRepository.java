package s4.spring.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import s4.spring.td5.entities.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>
{
    List<Category> findAll();
    Category findById(int id);
    Category findByName(String name);
}
