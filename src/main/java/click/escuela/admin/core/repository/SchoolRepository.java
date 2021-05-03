package click.escuela.admin.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.admin.core.model.School;

public interface SchoolRepository extends JpaRepository<School, UUID> {

}
