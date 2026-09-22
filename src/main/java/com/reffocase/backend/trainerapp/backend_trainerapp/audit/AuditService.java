package com.reffocase.backend.trainerapp.backend_trainerapp.audit;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationContext context;

    public AuditService(AuditLogRepository auditLogRepository, ObjectMapper objectMapper, ApplicationContext context) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
        this.context = context;
    }

    /**
     * Registrar CREACIÓN
     */
    public void logCreate(String entityName, Long entityId, Object createdObject) {
        String details = formatObjectToKeyValues(createdObject);
        saveLog("CREATE", entityName, entityId, details);
    }

    /**
     * Registrar ACTUALIZACIÓN (Compara Estado Anterior vs Estado Nuevo)
     */
    public void logUpdate(String entityName, Long entityId, Object oldState, Object newState) {
        String details = buildDiffDetails(oldState, newState);
        saveLog("UPDATE", entityName, entityId, details);
    }

    /**
     * Registrar ELIMINACIÓN
     */
    public void logDelete(String entityName, Long entityId, Object deletedObject) {
        String details = formatObjectToKeyValues(deletedObject);
        saveLog("DELETE", entityName, entityId, details);
    }

    /**
     * Obtiene el objeto real actual desde la base de datos a partir del audit log
     */
    @SuppressWarnings("unchecked")
    public Object getOriginalEntity(Long logId) {
        AuditLog log = auditLogRepository.findById(logId).orElse(null);
        if (log == null || log.getEntityId() == null || log.getEntityName() == null) {
            return null;
        }

        try {
            String entityName = log.getEntityName();
            if (entityName.endsWith("Dto") || entityName.endsWith("DTO")) {
                entityName = entityName.substring(0, entityName.length() - 3);
            }

            String repositoryBeanName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Repository";
            JpaRepository<?, Long> repository = (JpaRepository<?, Long>) context.getBean(repositoryBeanName);

            return repository.findById(log.getEntityId()).orElse(null);
        } catch (Exception e) {
            System.err.println("No se pudo obtener el repositorio para la entidad: " + log.getEntityName());
            return null;
        }
    }

    private void saveLog(String action, String entityName, Long entityId, String details) {
        try {
            String currentUsername = "SYSTEM";
            if (SecurityContextHolder.getContext() != null 
                    && SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            }

            AuditLog log = AuditLog.builder()
                    .username(currentUsername)
                    .action(action)
                    .entityName(entityName)
                    .entityId(entityId)
                    .timestamp(LocalDateTime.now())
                    .details(details)
                    .build();

            auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Error guardando el log de auditoría: " + e.getMessage());
        }
    }

    /**
     * Formatea un objeto completo a pares `campo = valor` (limpio, sin llaves ni JSON estricto)
     */
    private String formatObjectToKeyValues(Object obj) {
        if (obj == null) return "";
        StringBuilder sb = new StringBuilder();
        try {
            JsonNode node = objectMapper.valueToTree(obj);
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if (!value.isNull() && !value.isContainerNode()) {
                    sb.append(key).append(" = ").append(value.asText()).append("\n");
                }
            }
        } catch (Exception e) {
            return obj.toString();
        }
        return sb.toString().trim();
    }

    /**
     * Calcula la diferencia campo por campo entre el estado anterior y el nuevo
     */
    private String buildDiffDetails(Object oldState, Object newState) {
        if (oldState == null || newState == null) return "Modificación realizada";

        StringBuilder sb = new StringBuilder();
        try {
            // Si oldState ya es JsonNode, lo usamos directamente; si no, lo convertimos
            JsonNode oldNode = (oldState instanceof JsonNode) ? (JsonNode) oldState : objectMapper.valueToTree(oldState);
            JsonNode newNode = objectMapper.valueToTree(newState);

            Iterator<Map.Entry<String, JsonNode>> fields = newNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();

                // Ignorar metadatos y passwords
                if ("id".equalsIgnoreCase(key) || "password".equalsIgnoreCase(key) 
                        || "updatedAt".equalsIgnoreCase(key) || "createdAt".equalsIgnoreCase(key)) {
                    continue;
                }

                JsonNode newValNode = entry.getValue();
                JsonNode oldValNode = oldNode.get(key);

                if (newValNode == null || newValNode.isContainerNode()) {
                    continue;
                }

                String oldValStr = (oldValNode == null || oldValNode.isNull()) ? "" : oldValNode.asText().trim();
                String newValStr = newValNode.isNull() ? "" : newValNode.asText().trim();

                // Registrar ÚNICAMENTE los campos que sufrieron cambios reales
                if (!oldValStr.equals(newValStr)) {
                    sb.append(key)
                      .append(": '")
                      .append(oldValStr)
                      .append("' -> '")
                      .append(newValStr)
                      .append("'\n");
                }
            }
        } catch (Exception e) {
            return "Modificación realizada";
        }

        return sb.length() > 0 ? sb.toString().trim() : "Sin cambios detectados";
    }
}