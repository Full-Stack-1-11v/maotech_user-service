package cl.maotech.user_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import cl.maotech.user_service.model.Role;
import cl.maotech.user_service.repository.RoleRepository;

@SpringBootTest
@ActiveProfiles("test")
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockitoBean
    private RoleRepository roleRepository;

    @Test
    public void testSave(){
        // Given
        Role role = new Role(1, "Administrador");
        // When
        when(roleRepository.save(role)).thenReturn(role);
        // Then
        Role savedRole = roleService.save(role);
        assertEquals(role, savedRole);
        verify(roleRepository).save(role);
    }

    @Test
    public void testFindAll(){
        // Given
        List<Role> roles = new ArrayList<>();
        // When
        when(roleRepository.findAll()).thenReturn(roles);
        // Then
        List<Role> result = roleService.findAll();
        assertEquals(roles, result);
        assertEquals(0, result.size());
        verify(roleRepository).findAll();
    }

    @Test
    public void testFindById(){
        // Given
        int roleId = 1;
        Role role = new Role(roleId, "Administrador");
        // When
        when(roleRepository.findById(roleId)).thenReturn(java.util.Optional.of(role));
        // Then
        Role foundRole = roleService.findById(roleId);
        assertEquals(roleId, foundRole.getRoleId());
        assertEquals(role, foundRole);
        verify(roleRepository).findById(roleId);
    }

    @Test
    public void testDelete(){
        // Given
        int roleId = 1;
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(roleId, "Administrador"));
        // When
        when(roleRepository.existsById(roleId)).thenReturn(true);
        // Then
        roleService.delete(roleId);
        verify(roleRepository).deleteById(roleId);
    }
}
