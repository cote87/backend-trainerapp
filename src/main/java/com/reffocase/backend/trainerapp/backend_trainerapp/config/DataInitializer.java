package com.reffocase.backend.trainerapp.backend_trainerapp.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Permission;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Role;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.PermissionRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.RoleRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.UserRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.DocumentType;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Mode;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Province;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Thematic;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Trainer;
import com.reffocase.backend.trainerapp.backend_trainerapp.entities.Training;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.ThematicRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.TrainerRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.repositories.TrainingRepository;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.DocumentTypeService;
import com.reffocase.backend.trainerapp.backend_trainerapp.services.ProvinceService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProvinceService provinceRepository;

    @Autowired
    private DocumentTypeService documentTypeRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private ThematicRepository thematicRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("null")
    @Override
    public void run(String... args) {

        // Valores de la DB
        // predefinidos/////////////////////////////////////////////////////

        boolean produccion = true;

        // Lista de provincias
        if (provinceRepository.findAll().size() == 0) {
            provinceRepository.save(new Province("CAB", "C.A.B.A."));
            provinceRepository.save(new Province("BSA", "Buenos Aires"));
            provinceRepository.save(new Province("CAT", "Catamarca"));
            provinceRepository.save(new Province("COR", "Córdoba"));
            provinceRepository.save(new Province("CRR", "Corrientes"));
            provinceRepository.save(new Province("CHA", "Chaco"));
            provinceRepository.save(new Province("CHU", "Chubut"));
            provinceRepository.save(new Province("ENT", "Entre Ríos"));
            provinceRepository.save(new Province("FOR", "Formosa"));
            provinceRepository.save(new Province("JUJ", "Jujuy"));
            provinceRepository.save(new Province("PAM", "La Pampa"));
            provinceRepository.save(new Province("RIO", "La Rioja"));
            provinceRepository.save(new Province("MEN", "Mendoza"));
            provinceRepository.save(new Province("MIS", "Misiones"));
            provinceRepository.save(new Province("NEU", "Neuquén"));
            provinceRepository.save(new Province("RNE", "Río Negro"));
            provinceRepository.save(new Province("SAL", "Salta"));
            provinceRepository.save(new Province("SFE", "Santa Fe"));
            provinceRepository.save(new Province("SJU", "San Juan"));
            provinceRepository.save(new Province("SLU", "San Luis"));
            provinceRepository.save(new Province("SCR", "Santa Cruz"));
            provinceRepository.save(new Province("SDE", "Santiago del Estero"));
            provinceRepository.save(new Province("TDF", "Tierra del Fuego"));
            provinceRepository.save(new Province("TUC", "Tucumán"));
        }
        // Lista de tipo de documentos
        if (documentTypeRepository.findAll().size() == 0) {
            documentTypeRepository.save(new DocumentType("CI"));
            documentTypeRepository.save(new DocumentType("CUIT"));
            documentTypeRepository.save(new DocumentType("DNI"));
            documentTypeRepository.save(new DocumentType("ERRO"));
            documentTypeRepository.save(new DocumentType("LC"));
            documentTypeRepository.save(new DocumentType("LE"));
            documentTypeRepository.save(new DocumentType("LEM"));
            documentTypeRepository.save(new DocumentType("PAS"));
        }

        // Lista de permisos
        if (permissionRepository.findAll().size() == 0) {
            permissionRepository.save(new Permission("KEY_READ_THEMATICS"));
            permissionRepository.save(new Permission("KEY_WRITE_THEMATICS"));
            permissionRepository.save(new Permission("KEY_DELETE_THEMATICS"));
            permissionRepository.save(new Permission("KEY_READ_TRAINERS"));
            permissionRepository.save(new Permission("KEY_WRITE_TRAINERS"));
            permissionRepository.save(new Permission("KEY_DELETE_TRAINERS"));
            permissionRepository.save(new Permission("KEY_READ_TRAININGS"));
            permissionRepository.save(new Permission("KEY_WRITE_TRAININGS"));
            permissionRepository.save(new Permission("KEY_DELETE_TRAININGS"));
            permissionRepository.save(new Permission("KEY_READ_USERS"));
            permissionRepository.save(new Permission("KEY_WRITE_USERS"));
            permissionRepository.save(new Permission("KEY_DELETE_USERS"));
            permissionRepository.save(new Permission("KEY_READ_LOGGINUSER"));
            permissionRepository.save(new Permission("KEY_WRITE_LOGGINUSER"));
            permissionRepository.save(new Permission("KEY_READ_METRICS"));
        }

        if (roleRepository.findAll().size() == 0) {

            // Super Admin tiene control TOTAL
            LinkedHashSet<Permission> sadminPermissions = new LinkedHashSet<>(permissionRepository.findAll());

            // Admin tiene control del Super Admin con las siguientes limitaciones:
            // Solo ve usuarios del tipo USER y READER de su Provincia.
            // No puede ver métricas.
            Set<Permission> adminPermissions = new HashSet<>(listOfPermission("ROLE_ADMIN", sadminPermissions));

            // User es el usuario promedio, se diferencia de un Admin al tener las
            // siguientes limitaciones:
            // No tiene acceso de ningun tipo a Usuarios.
            Set<Permission> userPermissions = new HashSet<>(listOfPermission("ROLE_USER", sadminPermissions));

            // Reader es el usuario que solo puede leer informació, se diferencia del User
            // por tener las siguientes limitaciones:
            // No puede editar nada.
            // No tiene acceso a temáticas.
            Set<Permission> readerPermissions = new HashSet<>(listOfPermission("ROLE_READER", sadminPermissions));

            roleRepository.save(new Role("ROLE_SADMIN", sadminPermissions));
            roleRepository.save(new Role("ROLE_ADMIN", adminPermissions));
            roleRepository.save(new Role("ROLE_USER", userPermissions));
            roleRepository.save(new Role("ROLE_READER", readerPermissions));
        }

        // Se crea el SUPERUSER
        boolean createUsers = false;
        if (userRepository.findAll().size() == 0) {
            User sadmin = new User();
            sadmin.setUsername("sadmin");
            sadmin.setNickname("Super Admin");
            sadmin.setPassword(passwordEncoder.encode("12345"));
            sadmin.setRole(roleRepository.findById(1L).orElseThrow());
            sadmin.setEnabled(true);
            sadmin.setProvince(provinceRepository.findById(18L).orElseThrow());
            userRepository.save(sadmin);
            if(!produccion) createUsers = true;
        }

        // Se crean las temáticas iniciales que me pasaron por lista

        if (thematicRepository.findAll().size() == 0) {
            List<Thematic> thematics = new ArrayList<>();
            thematics.add(new Thematic("Docencia e Instrucción"));
            thematics.add(new Thematic("Protección del medioambiente"));
            thematics.add(new Thematic("Mecánica y mantenimiento"));
            thematics.add(new Thematic("Logística y gestión del material"));
            thematics.add(new Thematic("Sistemas de Comunicación"));
            thematics.add(new Thematic("Tecnología de la Información"));
            thematics.add(new Thematic("Salud y Bienestar"));
            thematics.add(new Thematic("Navegación"));
            thematics.add(new Thematic("Ceremonial, protocolo y Relaciones"));
            thematics.add(new Thematic("Instituciones"));
            thematics.add(new Thematic("Violencias"));
            thematics.add(new Thematic("Marco Legal y Reglamentos"));
            thematics.add(new Thematic("Administración"));
            thematics.add(new Thematic("Sexualidad y género"));
            thematics.add(new Thematic("Gestión de los Recursos humanos"));
            thematics.add(new Thematic("Idiomas"));
            thematics.add(new Thematic("Auditoría"));
            thematics.add(new Thematic("Liderazgo"));
            thematics.add(new Thematic("Cinotecnia y Adiestramiento Animal"));
            thematics.add(new Thematic("Orden Público"));
            thematics.add(new Thematic("Transporte y Seguridad vial"));
            thematics.add(new Thematic("Custodia y protección de personas"));
            thematics.add(new Thematic("Seguridad y control de crisis"));
            thematics.add(new Thematic("Armas y tiro"));
            thematics.add(new Thematic("Mediación y negociación"));
            thematics.add(new Thematic("Explosivos"));
            thematics.add(new Thematic("Protección marítima"));
            thematics.add(new Thematic("Prevención"));
            thematics.add(new Thematic("Operaciones especiales"));
            thematics.add(new Thematic("Video vigilancia"));
            thematics.add(new Thematic("Aviación"));
            thematics.add(new Thematic("Técnicas policiales básicas"));
            thematics.add(new Thematic("Reentrenamiento y Act. Pol."));
            thematics.add(new Thematic("Cooperación internacional"));
            thematics.add(new Thematic("Bomberos, seguridad contra incendios y rescate"));
            thematics.add(new Thematic("Sustancias peligrosas"));
            thematics.add(new Thematic("Salvamento y buceo"));
            thematics.add(new Thematic("Emergencias"));
            thematics.add(new Thematic("Investigación criminal"));
            thematics.add(new Thematic("Delitos tecnológicos"));
            thematics.add(new Thematic("Delito ambiental"));
            thematics.add(new Thematic("Delitos complejos"));
            thematics.add(new Thematic("Patrimonio cultural"));
            thematics.add(new Thematic("Trata de personas"));
            thematics.add(new Thematic("Lavado de activos"));
            thematics.add(new Thematic("Delitos económicos"));
            thematics.add(new Thematic("Policía científica"));
            thematics.add(new Thematic("Narcotráfico y drogas"));
            thematics.add(new Thematic("Inteligencia"));

            thematicRepository.saveAll(thematics);

        }

        // Datos de prueba, borrar en produccion//////////////////////////////////////////////////////////////////////

        if (!produccion) {
            if (createUsers) {
                User user = new User();
                List<Province> provinces = provinceRepository.findAll();

                for (int i = 0; i < 100; i++) {
                    int tipoRol = ThreadLocalRandom.current().nextInt(1, 3);
                    long rndProv = ThreadLocalRandom.current().nextInt(1, provinces.size() + 1);
                    user.setUsername("user" + i);
                    user.setNickname("user");
                    user.setPassword(passwordEncoder.encode("12345"));
                    user.setEnabled(true);
                    user.setProvince(
                            provinces.stream().filter(p -> p.getId().equals(rndProv)).findFirst().orElseThrow());
                    if (tipoRol == 1) {
                        user.setRole(roleRepository.findById(3L).orElseThrow());
                    } else {
                        user.setRole(roleRepository.findById(4L).orElseThrow());
                    }
                    userRepository.save(user);
                    user.setId(null);
                }
            }

            if (trainerRepository.count() == 0) {
                List<Province> provinces = provinceRepository.findAll();
                List<DocumentType> documentTypes = documentTypeRepository.findAll();
                List<Thematic> thematics = thematicRepository.findAll();
                Set<String> usedDocumentNumbers = new HashSet<>();
                Random random = new Random();

                List<String> nombres = new ArrayList<String>();
                nombres.add("José");
                nombres.add("Pablo");
                nombres.add("Julián");
                nombres.add("Pedro");
                nombres.add("Juán");
                nombres.add("Mirta");
                nombres.add("Sara");
                nombres.add("María");
                nombres.add("Pedro");
                nombres.add("Sofía");
                nombres.add("Norma");
                nombres.add("Mauricio");
                nombres.add("Cristina");
                nombres.add("Javier");
                nombres.add("Alberto");
                nombres.add("Patricia");
                nombres.add("Facundo");
                nombres.add("Romina");
                nombres.add("Marta");
                nombres.add("Laura");
                nombres.add("Raquel");
                nombres.add("Mario");

                List<String> apellidos = new ArrayList<String>();
                apellidos.add("Leguizamón");
                apellidos.add("Arteaga");
                apellidos.add("Candelaro");
                apellidos.add("Dust");
                apellidos.add("Perez");
                apellidos.add("Rodriguez");
                apellidos.add("Sanchez");
                apellidos.add("Almada");
                apellidos.add("Levin");
                apellidos.add("Gomez");
                apellidos.add("Leiva");
                apellidos.add("Silva");
                apellidos.add("Busaniche");
                apellidos.add("Cordoba");
                apellidos.add("Sassetti");
                apellidos.add("Estrada");
                apellidos.add("Valente");
                apellidos.add("Ramirez");
                apellidos.add("Ortega");
                apellidos.add("Mateoli");
                apellidos.add("Taborda");
                apellidos.add("Gago");

                for (int i = 0; i < 100; i++) {
                    Trainer trainer = new Trainer();
                    String nombreElegido = nombres.get(random.nextInt(nombres.size()));
                    trainer.setName(nombreElegido);
                    String apellidoElegido = apellidos.get(random.nextInt(nombres.size()));
                    trainer.setLastname(apellidoElegido);
                    trainer.setEmail(nombreElegido + apellidoElegido + i + "@example.com");

                    // Documento único
                    String docNum;
                    do {
                        docNum = String.valueOf(10000000 + random.nextInt(89999999));
                    } while (!usedDocumentNumbers.add(docNum));
                    trainer.setDocumentNumber(docNum);

                    trainer.setAreaCode(String.valueOf(100 + random.nextInt(900)));
                    trainer.setPhone(String.valueOf(1000000 + random.nextInt(9000000)));
                    trainer.setCv("CV de prueba " + i);
                    trainer.setEnabled(true);

                    // Relacionar con una provincia y tipo de documento aleatorio
                    trainer.setProvince(provinces.get(random.nextInt(provinces.size())));
                    trainer.setDocumentType(documentTypes.get(random.nextInt(documentTypes.size())));

                    // Temáticas entre 1 y 3
                    Set<Thematic> trainerThematics = new HashSet<>();
                    int thematicCount = 1 + random.nextInt(3);
                    Collections.shuffle(thematics);
                    for (int j = 0; j < thematicCount; j++) {
                        trainerThematics.add(thematics.get(j));
                    }
                    trainer.setThematics(trainerThematics);

                    trainerRepository.save(trainer);
                }
            }

            if (trainingRepository.count() == 0) {

                List<Thematic> thematics = thematicRepository.findAll();
                List<Province> provinces = provinceRepository.findAll();

                // Crear capacitaciones de prueba
                Training t1 = new Training();
                t1.setTitle("Capacitación en Java");
                t1.setStartDate(LocalDate.now().plusDays(3));
                t1.setOrganizer("José Pérez");
                t1.setDescription(
                        "<p>Aprenderás los conceptos básicos de Java y programación orientada a objetos.</p>");
                t1.setThematic(thematics.get(2));
                t1.setProvince(provinces.get(3));
                t1.setMode(Mode.HYBRID);

                Training t2 = new Training();
                t2.setTitle("Primeros Auxilios");
                t2.setStartDate(LocalDate.now().plusDays(7));
                t2.setOrganizer("María Gómez");
                t2.setDescription("<p>Capacitación sobre primeros auxilios y protocolos de emergencia.</p>");
                t2.setThematic(thematics.get(1));
                t2.setProvince(provinces.get(1));
                t2.setMode(Mode.ONLINE);

                Training t3 = new Training();
                t3.setTitle("Métodos de Enseñanza Moderna");
                t3.setStartDate(LocalDate.now().plusDays(10));
                t3.setOrganizer("Carlos Ruiz");
                t3.setDescription("<p>Explora nuevas estrategias pedagógicas para mejorar la educación.</p>");
                t3.setThematic(thematics.get(0));
                t3.setProvince(provinces.get(5));
                t3.setMode(Mode.ONSITE);

                trainingRepository.saveAll(List.of(t1, t2, t3));
            }
        }
    }

    private LinkedHashSet<Permission> listOfPermission(String role, LinkedHashSet<Permission> sadminPermissions) {
        LinkedHashSet<Permission> result = new LinkedHashSet<>();
        for (Permission permission : sadminPermissions) {
            switch (permission.getName()) {

                // Permisos para todos
                case "KEY_READ_TRAINERS":
                case "KEY_READ_TRAININGS":
                case "KEY_READ_LOGGINUSER":
                case "KEY_WRITE_LOGGINUSER":
                    result.add(permission);
                    break;

                // Permisos solo para user y admin
                case "KEY_READ_THEMATICS":
                case "KEY_WRITE_TRAINERS":
                case "KEY_WRITE_TRAININGS":
                case "KEY_DELETE_TRAINERS":
                case "KEY_DELETE_TRAININGS":
                    if (role == "ROLE_ADMIN" || role == "ROLE_USER")
                        result.add(permission);
                    break;

                // Permisos solo para admin
                case "KEY_READ_USERS":
                case "KEY_WRITE_USERS":
                case "KEY_DELETE_USERS":
                    if (role == "ROLE_ADMIN")
                        result.add(permission);
                    break;
            }
        }
        return result;
    }
}
