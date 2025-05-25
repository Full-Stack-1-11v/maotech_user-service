package cl.maotech.user_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.maotech.user_service.dto.AuthDTO;
import cl.maotech.user_service.dto.StatusEditDTO;
import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.dto.UserEditDTO;
import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.model.User;
import cl.maotech.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Integer id){
        return userRepository.findById(id).get();
    }

    public void delete(Integer id){
        userRepository.deleteById(id);
    }

    public List<User> findByStatusFalse(){
        return userRepository.findByStatusFalse();
    }

    public void deleteStatusFalse(){
        List<User> inactives = userRepository.findByStatusFalse();
        userRepository.deleteAll(inactives);
    }

    public List<UserDTO> findInactivesDto(){
        return userRepository.findInactivesAsDto();
    }

    //Las contraseñas se hashean con el microservicio Auth
    public boolean login(String email, String password){
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public UserDTO toDto(User user){
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setRut(user.getRut());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setStatus(user.getStatus());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

    public List<UserDTO> getAllAsDto(){
        return userRepository.findAllAsDto();
    }

    public UserEditDTO toEditDto(User user){
        if (user == null) {
            return null;
        }
        UserEditDTO editDTO = new UserEditDTO();

        editDTO.setUserId(user.getUserId());
        editDTO.setEmail(user.getEmail());
        editDTO.setFirstName(user.getFirstName());
        editDTO.setLastName(user.getLastName());

        return editDTO;
    }

    public User updateFromDto(UserEditDTO dto, User user) {
        if (dto == null || user == null) {
            return user;
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }

        return user;
    }

    public User updateStatusDto(StatusEditDTO dto, User user) {
        if (dto == null || user == null) {
            return user;
        }
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }

        return user;
    }

    public User updateRoleDto(Integer roleId, User user) {

        if (roleId != null) {
            Role role = new Role();
            role.setRoleId(roleId);
            user.setRole(role);       
        }

        return user;
    }

    public AuthDTO authDto(User user){
        if (user == null) {
            return null;
        }

        AuthDTO userDTO = new AuthDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }

}