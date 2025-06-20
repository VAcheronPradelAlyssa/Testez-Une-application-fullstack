package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SessionMapperImplTest {

    private SessionMapperImpl mapper;
    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);
        mapper = new SessionMapperImpl();
        mapper.teacherService = teacherService;
        mapper.userService = userService;
    }

     // Vérifie la conversion d'un SessionDto en Session (cas standard)
    @Test
    void testToEntity() {
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga");
        dto.setDescription("Cours de yoga");
        dto.setDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setTeacher_id(10L);
        dto.setUsers(Arrays.asList(100L, 101L));

        Teacher teacher = Teacher.builder().id(10L).firstName("John").lastName("Doe").build();
        User user1 = User.builder().id(100L).email("a@a.com").firstName("A").lastName("A").password("password").build();
        User user2 = User.builder().id(101L).email("b@b.com").firstName("B").lastName("B").password("password").build();

        when(teacherService.findById(10L)).thenReturn(teacher);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(user2);

        Session entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getTeacher_id(), entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        assertEquals(dto.getUsers().size(), entity.getUsers() != null ? entity.getUsers().size() : 0);
        assertTrue(entity.getUsers().stream().anyMatch(u -> u.getId().equals(100L)));
        assertTrue(entity.getUsers().stream().anyMatch(u -> u.getId().equals(101L)));
    }

    // Vérifie la conversion d'un Session en SessionDto (cas standard)
    @Test
    void testToDto() {
        Teacher teacher = Teacher.builder().id(10L).firstName("John").lastName("Doe").build();
        User user1 = User.builder().id(100L).email("a@a.com").firstName("A").lastName("A").password("password").build();
        User user2 = User.builder().id(101L).email("b@b.com").firstName("B").lastName("B").password("password").build();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga")
                .description("Cours de yoga")
                .date(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .build();

        SessionDto dto = mapper.toDto(session);

        assertNotNull(dto);
        assertEquals(session.getId(), dto.getId());
        assertEquals(session.getName(), dto.getName());
        assertEquals(session.getDescription(), dto.getDescription());
        assertEquals(session.getTeacher().getId(), dto.getTeacher_id());
        assertEquals(session.getUsers().size(), dto.getUsers().size());
        assertTrue(dto.getUsers().contains(user1.getId()));
        assertTrue(dto.getUsers().contains(user2.getId()));
    }

    // Vérifie la conversion d'une liste de SessionDto en liste de Session
    @Test
    void testToEntityList() {
        SessionDto dto1 = new SessionDto();
        dto1.setId(1L);
        dto1.setName("Yoga");
        dto1.setDescription("Cours de yoga");
        dto1.setDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        dto1.setCreatedAt(LocalDateTime.now());
        dto1.setUpdatedAt(LocalDateTime.now());
        dto1.setTeacher_id(10L);
        dto1.setUsers(Collections.singletonList(100L));

        SessionDto dto2 = new SessionDto();
        dto2.setId(2L);
        dto2.setName("Pilates");
        dto2.setDescription("Cours de pilates");
        dto2.setDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        dto2.setCreatedAt(LocalDateTime.now());
        dto2.setUpdatedAt(LocalDateTime.now());
        dto2.setTeacher_id(11L);
        dto2.setUsers(Collections.singletonList(101L));

        Teacher teacher1 = Teacher.builder().id(10L).firstName("John").lastName("Doe").build();
        Teacher teacher2 = Teacher.builder().id(11L).firstName("Jane").lastName("Smith").build();
        User user1 = User.builder().id(100L).email("a@a.com").firstName("A").lastName("A").password("password").build();
        User user2 = User.builder().id(101L).email("b@b.com").firstName("B").lastName("B").password("password").build();

        when(teacherService.findById(10L)).thenReturn(teacher1);
        when(teacherService.findById(11L)).thenReturn(teacher2);
        when(userService.findById(100L)).thenReturn(user1);
        when(userService.findById(101L)).thenReturn(user2);

        List<SessionDto> dtoList = Arrays.asList(dto1, dto2);
        List<Session> entityList = mapper.toEntity(dtoList);

        assertNotNull(entityList);
        assertEquals(2, entityList.size());
        assertEquals(dto1.getId(), entityList.get(0).getId());
        assertEquals(dto2.getId(), entityList.get(1).getId());
    }

     // Vérifie la conversion d'une liste de Session en liste de SessionDto
    @Test
    void testToDtoList() {
        Teacher teacher1 = Teacher.builder().id(10L).firstName("John").lastName("Doe").build();
        Teacher teacher2 = Teacher.builder().id(11L).firstName("Jane").lastName("Smith").build();
        User user1 = User.builder().id(100L).email("a@a.com").firstName("A").lastName("A").password("password").build();
        User user2 = User.builder().id(101L).email("b@b.com").firstName("B").lastName("B").password("password").build();

        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga")
                .description("Cours de yoga")
                .date(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher1)
                .users(Collections.singletonList(user1))
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Pilates")
                .description("Cours de pilates")
                .date(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher2)
                .users(Collections.singletonList(user2))
                .build();

        List<Session> entityList = Arrays.asList(session1, session2);
        List<SessionDto> dtoList = mapper.toDto(entityList);

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(session1.getId(), dtoList.get(0).getId());
        assertEquals(session2.getId(), dtoList.get(1).getId());
    }

    // Vérifie que les méthodes de mapping gèrent bien les cas null
    @Test
    void testNullCases() {
        assertNull(mapper.toEntity((SessionDto) null));
        assertNull(mapper.toDto((Session) null));
        assertNull(mapper.toEntity((List<SessionDto>) null));
        assertNull(mapper.toDto((List<Session>) null));
    }

    // Vérifie la conversion d'un SessionDto avec teacher_id null
     @Test
    void testToEntity_teacherIdNull() {
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga");
        dto.setDescription("Cours de yoga");
        dto.setDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        dto.setTeacher_id(null); // teacher_id null
        dto.setUsers(Arrays.asList(100L));

        User user1 = User.builder().id(100L).email("a@a.com").firstName("A").lastName("A").password("password").build();
        when(userService.findById(100L)).thenReturn(user1);

        Session entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getTeacher());
        assertEquals(1, entity.getUsers().size());
    }

    // Vérifie la conversion d'un SessionDto avec users null ou inconnus
    @Test
    void testToEntity_usersNullOrUnknown() {
        // users null
        SessionDto dtoNull = new SessionDto();
        dtoNull.setId(2L);
        dtoNull.setName("Yoga");
        dtoNull.setUsers(null);

        Session entityNull = mapper.toEntity(dtoNull);
        assertNotNull(entityNull);
        assertNotNull(entityNull.getUsers());
        assertTrue(entityNull.getUsers().isEmpty());

        // users contient un id inconnu
        SessionDto dtoUnknown = new SessionDto();
        dtoUnknown.setId(3L);
        dtoUnknown.setName("Yoga");
        dtoUnknown.setUsers(Arrays.asList(999L));
        when(userService.findById(999L)).thenReturn(null);

        Session entityUnknown = mapper.toEntity(dtoUnknown);
        assertNotNull(entityUnknown);
        assertEquals(1, entityUnknown.getUsers().size());
        assertNull(entityUnknown.getUsers().get(0));
    }

    // Vérifie la méthode privée sessionTeacherId pour les cas null
    @Test
void testSessionTeacherId_nullCases() throws Exception {
    // session null
    java.lang.reflect.Method method = mapper.getClass().getDeclaredMethod("sessionTeacherId", Session.class);
    method.setAccessible(true);

    assertNull(method.invoke(mapper, (Session) null));
    // teacher null
    Session sessionNoTeacher = Session.builder().id(1L).teacher(null).build();
    assertNull(method.invoke(mapper, sessionNoTeacher));
    // teacher id null
    Teacher teacherNoId = Teacher.builder().id(null).firstName("A").lastName("B").build();
    Session sessionTeacherNoId = Session.builder().id(2L).teacher(teacherNoId).build();
    assertNull(method.invoke(mapper, sessionTeacherNoId));
}

}