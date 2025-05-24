package cl.maotech.user_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.repository.RoleRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role findById(Integer id){
        return roleRepository.findById(id).get();
    }

    public void delete(Integer id){
        roleRepository.deleteById(id);
    }
}
