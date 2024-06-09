package core.database.repository;

import core.database.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SearchRepository extends JpaRepository<Search, UUID> {
}
