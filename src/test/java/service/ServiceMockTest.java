package service;

import domain.Homework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

public class ServiceMockTest {
    private Service service;
    @Mock
    private StudentXMLRepository studentXMLRepository;
    @Mock
    private HomeworkXMLRepository homeworkXMLRepository;
    @Mock
    private GradeXMLRepository gradeXMLRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        studentXMLRepository = Mockito.mock(StudentXMLRepository.class);
        homeworkXMLRepository = Mockito.mock(HomeworkXMLRepository.class);
        gradeXMLRepository = Mockito.mock(GradeXMLRepository.class);

        Mockito.when(homeworkXMLRepository.save(any(Homework.class))).thenThrow(ValidationException.class);
        Mockito.when(studentXMLRepository.delete(anyString())).thenReturn(null);

        service = new Service(studentXMLRepository, homeworkXMLRepository, gradeXMLRepository);
    }

    @Test
    public void saveHomeworkDeadlineCheck() {
        assertNotEquals( 1, service.saveHomework("092", "AI hazi", 19, 13), "Homework validator does not work well!");
    }

    @Test
    public void deleteStudentThatDoesntExist() {
        assertEquals(0, service.deleteStudent("-98"), "A non existent student was deleted!");
        verify(studentXMLRepository).delete("-98");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    void gradesWithNonExistentStudentAndHomework(String student){
        assertTrue( 1 != service.saveGrade(student, " ", 9.9, 12, "Well done!"), "Saving grades doesn't work correctly");
    }
}
