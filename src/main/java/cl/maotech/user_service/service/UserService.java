package cl.maotech.user_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
