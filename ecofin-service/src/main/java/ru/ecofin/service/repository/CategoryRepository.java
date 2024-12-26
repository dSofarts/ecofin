package ru.ecofin.service.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecofin.service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
