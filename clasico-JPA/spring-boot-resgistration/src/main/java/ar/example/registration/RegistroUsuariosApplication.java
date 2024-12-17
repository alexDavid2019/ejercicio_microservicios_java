package ar.example.registration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ar.example.registration.persistence.entity.RoleEntity;
import ar.example.registration.persistence.entity.RoleEnum;
import ar.example.registration.persistence.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class RegistroUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistroUsuariosApplication.class, args);
	}
		
	
    @Bean
    CommandLineRunner init(RoleRepository roleRepository) {
        return args -> {
            /* Create ROLES */  
        	/*
        	List<RoleEntity> list = new ArrayList<RoleEntity>();
        	list.add(new RoleEntity(UUID.randomUUID(), RoleEnum.ADMIN));
        	list.add(new RoleEntity(UUID.randomUUID(), RoleEnum.USER));
        	list.add(new RoleEntity(UUID.randomUUID(), RoleEnum.OTHER));
            roleRepository.save(list);
            */
        	List<RoleEntity> list = new ArrayList<RoleEntity>();
        	list.add(new RoleEntity(RoleEnum.ADMIN));
        	list.add(new RoleEntity(RoleEnum.USER));
        	list.add(new RoleEntity(RoleEnum.OTHER));
            roleRepository.save(list);
        };
    }
    
}
