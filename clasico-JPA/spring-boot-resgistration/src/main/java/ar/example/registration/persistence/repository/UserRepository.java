package ar.example.registration.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.example.registration.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	@Query("select e from UserEntity e where e.id = ?1")
    Optional<UserEntity> findByUUId(UUID id);

    Optional<UserEntity> findByEmail(String email);

}
