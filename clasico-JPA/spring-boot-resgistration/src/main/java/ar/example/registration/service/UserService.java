package ar.example.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ar.example.registration.dto.AuthCreateUserRequest;
import ar.example.registration.dto.AuthLoginRequest;
import ar.example.registration.dto.PhoneDTO;
import ar.example.registration.exception.ResourceNotFoundException;
import ar.example.registration.persistence.entity.PhoneEntity;
import ar.example.registration.persistence.entity.RoleEntity;
import ar.example.registration.persistence.entity.RoleEnum;
import ar.example.registration.persistence.entity.UserEntity;
import ar.example.registration.persistence.repository.PhoneRepository;
import ar.example.registration.persistence.repository.RoleRepository;
import ar.example.registration.persistence.repository.UserRepository;
import ar.example.registration.util.JwtUtils;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity getUser(UUID id) {
    	
        UserEntity userSaved = userRepository.findByUUId(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Entity not found with id " + id));
        userSaved.getRoles().size();
        userSaved.getPhones().size();
        return userSaved;
    }
    
    @Transactional
    public UserEntity updateUser(UUID id, UserEntity entityFromDTO) {
    	
        Optional<UserEntity> existingOptional = userRepository.findByUUId(id);
        if (existingOptional.isPresent()) {
        	UserEntity existingUser = existingOptional.get();
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("Entity not found with id " + id);
        }
    }

    @Transactional
    public UserEntity deleteUser(UUID id) {

        Optional<UserEntity> existingOptional = userRepository.findByUUId(id);
        
        if (existingOptional.isPresent()) {
        	UserEntity existingUser = existingOptional.get();
        	existingUser.setIsActive(false);
        	existingUser.setModified(new Date());
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("Entity not found with id " + id);
        }
    }
    
    @Transactional
    public UserDetails loadUserByEmail(String email) {

       Optional<UserEntity> userEntity = userRepository.findByEmail(email);
       
       if (!userEntity.isPresent()) {
    	   throw new UsernameNotFoundException("User email:" + email + " not exists.");
       }
       
       List<SimpleGrantedAuthority> authorityList = new ArrayList<SimpleGrantedAuthority>();

       for(final RoleEntity role:userEntity.get().getRoles()) {
   			authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())));
       }
       
       return new User(userEntity.get().getFullName(), 
        					userEntity.get().getPassword(), 
        					userEntity.get().getIsActive(), 
        					false, 
        					false, 
        					false, authorityList);
    }

    @Transactional
    public UserEntity createUser(AuthCreateUserRequest createUserRequest) {

        String fullname = createUserRequest.getName();
        String password = createUserRequest.getPassword();
        String email = createUserRequest.getEmail();
        List<String> rolesRequest = createUserRequest.getRolesRequest();
        List<PhoneDTO> phonesRequest = createUserRequest.getPhonesRequest();
        
        List<PhoneEntity> phonesEntityList = new ArrayList<PhoneEntity>();
        
        if (phonesRequest != null && !phonesRequest.isEmpty()) {
	        for(final PhoneDTO phone:phonesRequest) {
	        	phonesEntityList.add(new PhoneEntity(phone.getNumber(),phone.getCityCode(),phone.getCountryCode()));
	        }
        }
        
        List<RoleEnum> enumToQuery = new ArrayList<RoleEnum>();
        
        if (rolesRequest != null && !rolesRequest.isEmpty()) {
	        for(final String enumName:rolesRequest) {
	        	if (RoleEnum.values().equals(enumName)) {
	        		enumToQuery.add(RoleEnum.valueOf(enumName) );
	        	}
	        }
	    }
        if (enumToQuery.isEmpty()) enumToQuery.add(RoleEnum.USER);
        
        List<RoleEntity> roleEntityList = roleRepository.findByRoleEnumIn(enumToQuery);
        
        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }

        Optional<UserEntity> userSavedByEmail = userRepository.findByEmail(email);
        
        if (userSavedByEmail.isPresent()) {
     	   throw new IllegalArgumentException("User email:" + email + " exists.");
        }
        
        UserEntity userEntity = new UserEntity();
        userEntity.setFullName(fullname);
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setRoles(roleEntityList.stream().collect(Collectors.toSet())); 
        userEntity.setIsActive(true);
        userEntity.setCreated(new Date());
        userEntity.setPhones(phonesEntityList.stream().collect(Collectors.toSet()));
        
        UserEntity userSaved = userRepository.save(userEntity);
                
    	for(PhoneEntity phone:userEntity.getPhones()) {
    		phone.setUser(userSaved);
    		phoneRepository.save(phone);
    	}
    	        
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

        for(final RoleEntity role: userSaved.getRoles()) {
        	authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())));
        }
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);

        //String accessToken = jwtUtils.createToken(authentication);
        String accessToken = jwtUtils.generarToken(authentication);
        jwtUtils.checkToken(accessToken);
        
        userSaved.setToken(accessToken);
        userSaved.setModified(new Date());
        userSaved = userRepository.save(userSaved);

        return userSaved;
    }

    public UserEntity loginUser(AuthLoginRequest authLoginRequest) {

    	String password = authLoginRequest.getPassword();
        String email = authLoginRequest.getEmail();
        
        Authentication authentication = this.authenticate(email, password);
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //String accessToken = jwtUtils.createToken(authentication);
        String accessToken = jwtUtils.generarToken(authentication);

        UserEntity userSaved = userRepository.findByEmail(email)
        		.orElseThrow(() -> new UsernameNotFoundException("User email:" + email + " not exists."));
        
        userSaved.setToken(accessToken);
        userSaved.setModified(new Date());
        userSaved.setLastAccess(new Date());
        UserEntity userFinalSaved = userRepository.save(userSaved);

        userFinalSaved.getRoles().size();
        userFinalSaved.getPhones().size();
       
        return userFinalSaved;
    }

    public Authentication authenticate(String email, String password) {
        
    	UserDetails userDetails = this.loadUserByEmail(email);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid email or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }
        
        return new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
    }
    
}
