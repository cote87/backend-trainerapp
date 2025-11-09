package com.reffocase.backend.trainerapp.backend_trainerapp.auth.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto.UserDto;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.dto.mapper.DtoMapperUser;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("null")
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        Optional<User> user = repository.findByUsername(username);
        return user;
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return DtoMapperUser.builder().setUser(repository.save(user)).build();
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findUsersPage(int page, int size, Long provinceId, String name, boolean excludeAdmin) {

        // Se determinan las condiciones de ordenación de los resultados
        Pageable pageable;
        pageable = PageRequest.of(page, size);

        Page<User> usersPage = repository.findUsersPage(provinceId, name, excludeAdmin, pageable);

        Page<UserDto> usersDtoPage = usersPage.map(user -> DtoMapperUser.builder().setUser(user).build());

        return usersDtoPage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsersPDF(Long provinceId, String name, boolean excludeAdmin) {

        List<User> users = repository.findUsers(provinceId, name, excludeAdmin);

        List<UserDto> usersDto = users.stream()
                .map(user -> DtoMapperUser.builder().setUser(user).build())
                .toList();

        return usersDto;
    }
}