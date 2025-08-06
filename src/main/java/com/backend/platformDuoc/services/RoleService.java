package com.backend.platformDuoc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.platformDuoc.models.Rol;
import com.backend.platformDuoc.repository.RoleRepository;
import com.backend.platformDuoc.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository rolRepository;

    @Autowired
    private UserRepository usuarioRepository;

    public Rol findById(Integer id){
        return rolRepository.findById(id).orElse(null);
    }

    public List<Rol> findAll(){
        return rolRepository.findAll();
    }

    public Rol save(Rol rol){return rolRepository.save(rol);}

    public void delete(Integer id){
        rolRepository.deleteById(id);
    }
    
    public Rol actualizar(Integer id, Rol nuevoRol) {
        Rol rolExistente = rolRepository.findById(id).orElse(null);
        if (rolExistente == null) {
            return null;
        }
        
        rolExistente.setRoleName(nuevoRol.getRoleName());
        return rolRepository.save(rolExistente);
    }

    public boolean tieneUsuariosAsociados(Integer rolId) {
    return usuarioRepository.existsByRole_Id(rolId);
    }
}