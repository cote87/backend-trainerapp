package com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Busqueda de los usuarios en paginas
    @Query("""
                SELECT DISTINCT u
                FROM User u
                WHERE (:provinceId IS NULL OR u.province.id = :provinceId)
                  AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
                  AND u.role.name <> 'ROLE_SADMIN'
                  AND (:excludeAdmin = false OR u.role.name <> 'ROLE_ADMIN')
            """)
    Page<User> findUsersPage(
            @Param("provinceId") Long provinceId,
            @Param("username") String username,
            @Param("excludeAdmin") boolean excludeAdmin,
            Pageable pageable);

    // Busqueda de los usuarios completa (para el pdf)
    @Query("""
                SELECT DISTINCT u
                FROM User u
                WHERE (:provinceId IS NULL OR u.province.id = :provinceId)
                  AND (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
                  AND u.role.name <> 'ROLE_SADMIN'
                  AND (:excludeAdmin = false OR u.role.name <> 'ROLE_ADMIN')
            """)
    List<User> findUsers(
            @Param("provinceId") Long provinceId,
            @Param("username") String username,
            @Param("excludeAdmin") boolean excludeAdmin);




    Page<User> findByRole_NameNot(String roleName, Pageable pageable);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.province.id = :provinceId")
    List<User> findByProvinceId(@Param("provinceId") Long provinceId);

    @Query("SELECT u FROM User u WHERE u.province.id = :provinceId AND u.role.name NOT IN ('ROLE_SADMIN','ROLE_ADMIN')")
    Page<User> findPageByProvinceId(@Param("provinceId") Long provinceId, Pageable pageable);

}
