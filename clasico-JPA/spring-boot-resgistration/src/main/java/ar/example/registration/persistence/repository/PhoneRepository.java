package ar.example.registration.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.example.registration.persistence.entity.PhoneEntity;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneEntity, UUID> {
    @Query("select e from PhoneEntity e where e.id = ?1")
    Optional<PhoneEntity> findByUUId(UUID id);
    
    List<PhoneEntity> findByUserId(UUID id);
    
}
