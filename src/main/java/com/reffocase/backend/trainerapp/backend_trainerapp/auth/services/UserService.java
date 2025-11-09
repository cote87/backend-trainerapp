package com.reffocase.backend.trainerapp.backend_trainerapp.auth.services;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto.UserDto;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;

public interface UserService {

    //Util para generar el pdf
    public List<UserDto> findUsersPDF(Long provinceId, String name, boolean excludeAdmin);
    //Busqueda de los usuarios por paginas
    public Page<UserDto> findUsersPage(int page, int size, Long provinceId, String name, boolean excludeAdmin);
    //Creado para uso interno, como por ejemplo buscar un password o algun dato sensible
    public Optional<User> findByUsername(String username);
    //Creado para busacar un usuario en particular sin datos sensibles
    public Optional<User> findById(Long id);
    //Crear o editar un usuario
    public UserDto save(User user);
    //Eliminar un usuario
    void remove(Long id);

}
