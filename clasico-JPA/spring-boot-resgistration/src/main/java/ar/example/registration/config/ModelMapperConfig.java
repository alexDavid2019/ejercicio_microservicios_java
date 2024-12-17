package ar.example.registration.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ar.example.registration.dto.RoleDTO;
import ar.example.registration.persistence.entity.RoleEntity;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
        	.setMatchingStrategy(MatchingStrategies.STANDARD);

        // Fix Error: Converter org.modelmapper.internal.converter.MergingCollectionConverter
        // failed to convert org.hibernate.collection.internal.PersistentSet to java.util.Set
        modelMapper.getConfiguration().setPropertyCondition(new Condition<Object, Object>() {
            public boolean applies(MappingContext<Object, Object> context) {
                //if the object is a PersistentCollection could be not initialized 
                //in case of lazy strategy, in this case the object will not be mapped:
                return (!(context.getSource() instanceof PersistentCollection) 
                || ((PersistentCollection)context.getSource()).wasInitialized());
            }
        });        
        modelMapper.createTypeMap(RoleEntity.class, RoleDTO.class)
    	.addMapping(src -> src.getRoleEnum(), RoleDTO::setName);
        
    	modelMapper.createTypeMap(RoleDTO.class,RoleEntity.class)
    	/*.addMapping(src -> src.getName(), RoleEntity::setRoleEnum)*/
        .addMappings(mapping -> mapping.when(Conditions.isNotNull())
                .map(RoleDTO::getName, RoleEntity::setRoleEnum));

        return modelMapper;
    }
   
}