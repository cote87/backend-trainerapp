package com.reffocase.backend.trainerapp.backend_trainerapp.audit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;

@Component
@Configurable
public class AuditEntityListener {

    private static ObjectFactory<AuditService> auditServiceObjectFactory;
    private static ObjectMapper objectMapper = new ObjectMapper();

    // Guardamos una captura JSON del estado viejo para no sufrir el problema de referencias compartidas en Hibernate
    private static final Map<String, JsonNode> oldStateCache = new ConcurrentHashMap<>();

    @Autowired
    public void setAuditServiceObjectFactory(ObjectFactory<AuditService> auditServiceObjectFactory) {
        AuditEntityListener.auditServiceObjectFactory = auditServiceObjectFactory;
    }

    /**
     * Se ejecuta cuando la entidad se lee desde la BD.
     * Guarda un SNAPSHOT congelado en JSON del estado original.
     */
    @PostLoad
    public void onPostLoad(Object entity) {
        if (isAuditable(entity)) {
            String key = buildCacheKey(entity);
            if (key != null) {
                try {
                    oldStateCache.put(key, objectMapper.valueToTree(entity));
                } catch (Exception e) {
                    // Ignorar errores de serialización previa
                }
            }
        }
    }

    @PostPersist
    public void onPostPersist(Object entity) {
        if (isAuditable(entity)) {
            AuditService auditService = getAuditService();
            if (auditService != null) {
                Long id = extractEntityId(entity);
                auditService.logCreate(entity.getClass().getSimpleName(), id, entity);
            }
        }
    }

    @PreUpdate
    public void onPreUpdate(Object entity) {
        if (isAuditable(entity)) {
            String key = buildCacheKey(entity);
            if (key != null && !oldStateCache.containsKey(key)) {
                try {
                    oldStateCache.put(key, objectMapper.valueToTree(entity));
                } catch (Exception e) {
                    // Ignorar
                }
            }
        }
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {
        if (isAuditable(entity)) {
            AuditService auditService = getAuditService();
            if (auditService != null) {
                Long id = extractEntityId(entity);
                String key = buildCacheKey(entity);
                JsonNode oldSnapshot = (key != null) ? oldStateCache.remove(key) : null;

                auditService.logUpdate(entity.getClass().getSimpleName(), id, oldSnapshot, entity);
            }
        }
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        if (isAuditable(entity)) {
            AuditService auditService = getAuditService();
            if (auditService != null) {
                Long id = extractEntityId(entity);
                auditService.logDelete(entity.getClass().getSimpleName(), id, entity);
            }
        }
    }

    private boolean isAuditable(Object entity) {
        return !(entity instanceof AuditLog);
    }

    private String buildCacheKey(Object entity) {
        Long id = extractEntityId(entity);
        if (id != null) {
            return entity.getClass().getSimpleName() + "_" + id;
        }
        return null;
    }

    private Long extractEntityId(Object entity) {
        try {
            var method = entity.getClass().getMethod("getId");
            Object result = method.invoke(entity);
            if (result instanceof Number) {
                return ((Number) result).longValue();
            }
        } catch (Exception e) {
            // Sin método getId()
        }
        return null;
    }

    private AuditService getAuditService() {
        if (auditServiceObjectFactory != null) {
            return auditServiceObjectFactory.getObject();
        }
        return null;
    }
}