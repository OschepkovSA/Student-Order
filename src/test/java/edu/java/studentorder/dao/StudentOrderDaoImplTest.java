package edu.java.studentorder.dao;

import edu.java.studentorder.domain.*;
import edu.java.studentorder.exception.DaoException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class StudentOrderDaoImplTest {

    @BeforeClass
    public static void startUp() throws Exception {
        DBInit.startUp();
    }

    @Test
    public void saveStudentOrder() throws DaoException {
        StudentOrder so = buildStudentOrder(10);
        Long id = new StudentOrderDaoImpl().saveStudentOrder(so);
    }

    @Test(expected = DaoException.class)
    public void saveStudentOrderError() throws DaoException {
        StudentOrder so = buildStudentOrder(10);
        so.getHusbund().setSurName(null);
        Long id = new StudentOrderDaoImpl().saveStudentOrder(so);
    }

    @Test
    public void getStudentOrders() throws DaoException {
        List<StudentOrder> list = new StudentOrderDaoImpl().getStudentOrders();

    }

    public StudentOrder buildStudentOrder(long id) {
        StudentOrder so = new StudentOrder();
        so.setStudentOrderId(id);
        so.setMarriageCertificateId("" + (12345600 + id));
        so.setMarriageDate(LocalDate.of(2016, 7, 4));

        RegisterOffice registerOffice = new RegisterOffice(1L, "", "");
        so.setMarriageOffice(registerOffice);

        Street street = new Street(1L, "Волгоградская улица");

        Address address = new Address("195000", street, "10", "2", "141");

        Adult husbund = new Adult("Андреев", "Алексей", "Васильевич", LocalDate.of(1990, 10,
                10));
        husbund.setPassportSeries("1234");
        husbund.setPassportNumber("431876");
        husbund.setIssueDate(LocalDate.of(2010, 6, 6));
        PassportOffice po1 = new PassportOffice(1L, "", "");
        husbund.setPassportOffice(po1);
        husbund.setStudentId("" + (10000 + id));
        husbund.setAddress(address);
        husbund.setUniversity(new University(2L, "SamGTU"));
        husbund.setStudentId("12355");

        Adult wife = new Adult("Андреева", "Ирина", "Петровна", LocalDate.of(1990, 10, 10));
        wife.setPassportSeries("3134");
        wife.setPassportNumber("591876");
        wife.setIssueDate(LocalDate.of(2012, 4, 8));
        PassportOffice po2 = new PassportOffice(2L, "", "");
        wife.setPassportOffice(po2);
        wife.setStudentId("" + (20000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(3L, "SamGU"));
        wife.setStudentId("54111");

        RegisterOffice ro = new RegisterOffice(1L, "", "");

        Child child1 = new Child("Андреева", "Евгения", "Алексеевна", LocalDate.of(2015, 1,
                30));
        child1.setCertificateNumber("8764123");
        child1.setIssueDate(LocalDate.of(2015, 2, 20));
        child1.setRegisterOffice(ro);
        child1.setAddress(address);

        Child child2 = new Child("Андреев", "Сергей", "Алексеевич",
                LocalDate.of(2016, 10, 25));
        child2.setCertificateNumber("9864123");
        child2.setIssueDate(LocalDate.of(2016, 11, 20));
        child2.setRegisterOffice(ro);
        child2.setAddress(address);

        so.setHusbund(husbund);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);

        return so;
    }
}