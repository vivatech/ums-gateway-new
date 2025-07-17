package com.vivatech.ums_api_gateway.repository;

import com.vivatech.ums_api_gateway.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findTopByOrderByIdDesc();

    @Query("SELECT COUNT(s.id) From Student s where s.sectionId=?1")
    Integer countBySectionId(Integer sectionId);

    Student findByRegistrationNo(String registrationNo);

    Student findByRegistrationNoContaining(String registrationNo);

    Student findByAdmissionNo(String admissionNo);

    Student findByLastSchoolCertificateNo(String certificateNo);

    Student findByContactNoAndStatus(String phoneNo, String status);

    Student findByEmailAndStatus(String email, String status);

    List<Student> findByAllottedDepartment(Integer departmentId);

    List<Student> findByBatchIdAndSemesteridAndSectionId(Integer batchId, Integer semesterId, Integer sectionId);

    List<Student> findByAllottedDepartmentAndBatchIdAndSemesteridAndSectionIdAndImagePath(Integer departmentId, Integer batchId, Integer semesterId, Integer sectionId, String imagePath);

    @Query(value = "select allotteddepartment  from student where registrationno =?1", nativeQuery = true)
    String findByAllotteddepartmentId(String regNo);

    @Query(value = "select student.batchid  from ums.student where registrationno =?1", nativeQuery = true)
    String findByBatchId(String regNo);

    @Query(value = "select student.semesterid  from ums.student where registrationno =?1", nativeQuery = true)
    Integer findBySemesterId(String regNo);

    @Query(value = "SELECT s.id, s.registrationno regitrationNo, s.firstname, s.lastname, s.batchid, b.name batchname FROM ums.student s, ums.batch b where s.batchid = b.id and s.batchid =?1 and s.semesterid =?2", nativeQuery = true)
    List<Object[]> getStudentList(Integer batchid, Integer semId);

    @Query(value = "select allotteddepartment from ums.student where registrationno =?1", nativeQuery = true)
    Integer findByAllotteddepartments(String regNo);

    List<Student> findBySemesterid(Integer semesterId);

    @Query(value = "SELECT s.* " + "FROM studentcourses sc, student s, instructorsload i " + "where s.registrationno=sc.studentregno and sc.semesterid=i.semesterid " + "and sc.courseid=i.courseid and sc.section=i.sectionid and i.semesterid=?1 and i.courseid=?2 and i.sectionid=?3 and s.status='ACTIVE' and sc.grade is null", nativeQuery = true)
    List<Student> getStudentListForAttendance(Integer semesterId, Integer courseId, Integer sectionId);

    @Query(value = "SELECT firstname from ums.student where registrationno=?1", nativeQuery = true)
    String findByName(String StudentId);

    Student findByContactNo(String contactNo);

    List<Student> findByBatchId(Integer batchNo);

    List<Student> findTop10ByRegistrationNoContaining(String registrationNo);

    List<Student> findBySectionId(Integer sectionId);

    List<Student> findByStatusOrderByCreatedDateDesc(String status);

    @Query(value = "SELECT COUNT(*), sh.name, d.name departmentname,d.description departmentDesc FROM student s, shifts sh, departments d where s.shiftid = sh.id and s.status = 'ACTIVE' and s.allotteddepartment=?1 and  d.id = s.allotteddepartment and s.academicYear =?2 GROUP BY sh.id", nativeQuery = true)
    List<Object> countStudenAndFilterByAcademicYear(Integer id, String acad);

    @Query(value = "SELECT COUNT(*)FROM student", nativeQuery = true)
    Integer findByAllData();

    @Query(value = "SELECT COUNT(*) FROM ums.student where studenttype =?1 and status='ACTIVE'", nativeQuery = true)
    Integer findByStudentType(String studentType);

    @Query(value = "SELECT COUNT(*) FROM student s where s.status = 'ACTIVE' and s.studenttype =?1 and s.academicYear =?2", nativeQuery = true)
    Integer findByStudentTypeAndAcademicYear(String studentType, String acad);

    @Query(value = "SELECT COUNT(*) FROM ums.student where status =?1", nativeQuery = true)
    Integer findByStudentStatus(String status);

    @Query(value = "SELECT COUNT(*) FROM student s where s.status =?1 and s.academicYear =?2", nativeQuery = true)
    Integer findByStudentStatusAndAcademicYear(String status, String acad);

    List<Student> findByBatchIdAndSemesteridAndShiftIdAndSectionId(Integer batid, Integer semId, Integer shiftId, Integer sectionId);

    List<Student> findByBatchIdAndSemesteridAndSectionIdNotNull(Integer batid, Integer semId);

    List<Student> findByBatchIdAndSemesteridAndShiftIdAndSectionIdNotNull(Integer batid, Integer semId, Integer shiftId);

    List<Student> findAllByStatus(String active);
    List<Student> findByAllottedDepartmentAndBatchIdAndSemesteridAndSectionIdAndShiftIdAndStudentTypeAndStatus(Integer allottedDepartment, Integer batchId, Integer semesterid, Integer sectionId, Integer shiftId, String studentType, String active);
    List<Student> findByAllottedDepartmentAndStatus(Integer id, String active);
    List<Student> findByAllottedDepartmentAndBatchIdAndSemesteridAndStatus(Integer allottedDepartment, Integer batchId, Integer semesterid, String status);
    List<Student> findByAllottedDepartmentAndStatusAndAcademicYear(Integer id, String active, String academicYear);

    @Query(nativeQuery = true, value = "select * from student where batchid is not null")
    List<Student> findStudentWithBatchNotNull();

    List<Student> findTop10ByAdmissionNoContaining(String admissionNo);

    List<Student> findTop10ByFirstNameContainingAndStatus(String studentName, String studentStatusPending);

    List<Student> findByBatchIdAndStudentTypeAndStatus(Integer batchNo, String studentType, String status);

    @Query("SELECT COUNT(s.id) From Student s where s.sectionId=?1 and s.semesterid=?2")
    Integer countBySectionIdAndSemesterId(Integer sectionId, Integer semesterId);

    List<Student> findByRegistrationNoIn(List<String> uniqueStudentRegNo);
}
