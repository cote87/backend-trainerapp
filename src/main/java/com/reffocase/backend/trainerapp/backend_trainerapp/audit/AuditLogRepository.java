package com.reffocase.backend.trainerapp.backend_trainerapp.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long>{

}
