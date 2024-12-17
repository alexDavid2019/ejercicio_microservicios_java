package ar.example.registration.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.example.registration.dto.ApiResponse;
import ar.example.registration.dto.AuthCreateUserRequest;
import ar.example.registration.dto.AuthLoginRequest;
import ar.example.registration.dto.UserDTO;
import ar.example.registration.exception.ResourceNotFoundException;
import ar.example.registration.persistence.entity.UserEntity;
import ar.example.registration.service.UserService;
import ar.example.registration.util.ConvertUtil;
import ar.example.registration.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1.0/api/users")
public class UserController {

    @Autowired UserService userService;

    @Autowired ModelMapper modelMapper;

    @ApiOperation(value = "Recuperar informacion del usuario mediante ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(
            @PathVariable String id,
            HttpServletRequest request) {
    	
    	if (!ConvertUtil.tryConvertStringToUUID(id)) {
            throw new ResourceNotFoundException("Invalid request with id " + id + ". Format is not valid.");
    	}
    	
    	UUID uuid = UUID.fromString(id);
    	
    	UserDTO responseDTO = this.convertToDto(userService.getUser(uuid));
        return ResponseEntity.ok(ResponseUtil.success(responseDTO, "User successfully", request.getRequestURI()));
    }

    @ApiOperation(value = "Actualizacion del usuario mediante ID.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO dto,
            HttpServletRequest request) {
    	
    	if (!ConvertUtil.tryConvertStringToUUID(id)) {
            throw new ResourceNotFoundException("Invalid request with id " + id + ". Format is not valid.");
    	}
    	
    	UUID uuid = UUID.fromString(id);
    
    	UserDTO responseDTO = this.convertToDto(userService.updateUser(uuid, this.convertToEntity(dto)));
        return ResponseEntity.ok(ResponseUtil.success(responseDTO, "User updated successfully", request.getRequestURI()));
    }

    @ApiOperation(value = "Creacion de un nuevo usuario.")
    @PostMapping("/")
    public ResponseEntity<ApiResponse<UserDTO>> newUser(
            @RequestBody AuthCreateUserRequest dto,
            HttpServletRequest request) {    	
    	UserDTO responseDTO = this.convertToDto(userService.createUser(dto));
        return ResponseEntity.ok(
        			ResponseUtil.success(responseDTO, "User created successfully", request.getRequestURI())
        		);
    }

    @ApiOperation(value = "Inhabilitacion del usuario mediante ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deleteUser(
            @PathVariable String id,
            HttpServletRequest request) {
    
    	if (!ConvertUtil.tryConvertStringToUUID(id)) {
            throw new ResourceNotFoundException("Invalid request with id " + id + ". Format is not valid.");
    	}
    	
    	UUID uuid = UUID.fromString(id);
    
    	UserDTO responseDTO = this.convertToDto(userService.deleteUser(uuid));
        return ResponseEntity.ok(ResponseUtil.success(responseDTO, "User deleted successfully", request.getRequestURI()));
    }

    @ApiOperation(value = "LogIn mediante usuario y password con retorno de Token JWT.")
    @PostMapping("/log-in")
    public ResponseEntity<ApiResponse<UserDTO>> login(
    				@RequestBody @Valid AuthLoginRequest userRequest,
    				HttpServletRequest request){
    	
    	UserDTO responseDTO = this.convertToDto(userService.loginUser(userRequest));
    	
        return ResponseEntity.ok(ResponseUtil.success(responseDTO, "User logged successfully", request.getRequestURI()));
    }
    
    private UserDTO convertToDto(UserEntity entity) {
    	
    	UserDTO dto = modelMapper.map(entity, UserDTO.class);
    	//dto.setDate(ConvertUtil.convertDateToString(entity.getDate(), this.getTimezone()));
        return dto;
    }
    
    private UserEntity convertToEntity(UserDTO dto) {
    	UserEntity entity = modelMapper.map(dto, UserEntity.class);
        //entity.setDate(ConvertUtil.convertStringToDate(dto.getDateConverted,this.getTimezone()));
        return entity;
    }
}
