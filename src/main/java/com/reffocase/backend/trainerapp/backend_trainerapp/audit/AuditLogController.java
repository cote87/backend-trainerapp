package com.reffocase.backend.trainerapp.backend_trainerapp.audit;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;
    private final AuditService auditService;

    public AuditLogController(AuditLogRepository auditLogRepository, AuditService auditService) {
        this.auditLogRepository = auditLogRepository;
        this.auditService = auditService;
    }

    /**
     * Búsqueda paginada con filtros opcionales por username, action y/o entityName
     */
    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        AuditLog filter = AuditLog.builder()
                .username(username)
                .action(action)
                .entityName(entity)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("action", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("entityName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Page<AuditLog> logs = auditLogRepository.findAll(Example.of(filter, matcher), pageable);
        return ResponseEntity.ok(logs);
    }

    /**
     * Obtiene el objeto real de la base de datos asociado al audit log por su ID
     */
    @GetMapping("/{id}/entity")
    public ResponseEntity<?> getEntityFromAuditLog(@PathVariable Long id) {
        Object entity = auditService.getOriginalEntity(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }
}