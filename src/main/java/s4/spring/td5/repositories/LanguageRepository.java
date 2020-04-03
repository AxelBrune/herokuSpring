package s4.spring.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import s4.spring.td5.entities.Language;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer>
{
    List<Language> findAll();
    Language findByName(String name);
    List<Language> findById(int id);
}
