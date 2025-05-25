package cl.maotech.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.maotech.user_service.dto.UserDTO;
import cl.maotech.user_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.status = false")
    List<User> findByStatusFalse();
    
    User findByEmail(String email);

    @Query("SELECT new cl.maotech.user_service.dto.UserDTO(u.userId, u.email, u.rut, u.firstName, u.lastName, u.status, u.role) FROM User u")
    List<UserDTO> findAllAsDto();

    @Query("SELECT new cl.maotech.user_service.dto.UserDTO(u.userId, u.email, u.rut, u.firstName, u.lastName, u.status, u.role) FROM User u WHERE u.status = false")
    List<UserDTO> findInactivesAsDto();
}
