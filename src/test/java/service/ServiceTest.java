package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.GradeValidator;
import validation.HomeworkValidator;
import validation.StudentValidator;
import validation.Validator;

public class ServiceTest {
    private static Service service;
    private static StudentXMLRepository fileRepository1;

    @BeforeAll
    public static void setUp() throws Exception {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    public void findAllStudents() {
        assertEquals( service.findAllStudents(), fileRepository1.findAll(), "The Service's findAllStudents function doesn't work correctly!");
    }

    @Test
    public void saveStudentHappyCase() {
        assertEquals( 1, service.saveStudent("8", "Gergo", 211), "A student wasn't saved successfully!");
        assertEquals(1, service.deleteStudent("8"), "A student wasn't deleted successfully!");
    }

    @Test
    public void saveHomeworkDeadlineCheck() {
        assertNotEquals( 1, service.saveHomework("092", "AI hazi", 19, 13), "Homework validator does not work well!");
    }

    @Test
    public void deleteStudentThatDoesntExist() {
        assertEquals(0, service.deleteStudent("-98"), "A non existent student was deleted!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  "})
    void gradesWithNonExistentStudentAndHomework(String student){
        assertTrue( 1 != service.saveGrade(student, " ", 9.9, 12, "Well done!"), "Saving grades doesn't work correctly");
    }

}