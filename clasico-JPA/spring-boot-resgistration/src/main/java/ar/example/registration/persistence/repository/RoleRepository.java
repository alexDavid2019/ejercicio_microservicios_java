package ar.example.registration.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.example.registration.persistence.entity.RoleEntity;
import ar.example.registration.persistence.entity.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    @Query("select e from RoleEntity e where e.id = ?1")
    Optional<RoleEntity> findByUUId(UUID id);

    List<RoleEntity> findByRoleEnumIn(List<RoleEnum> roles);
}
