package com.attendance.repository;
import com.attendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository //tells springboot this is data access component. Create a spring bean for it so i can inject it into controller later
public interface StudentRepository extends JpaRepository<Student, String> { 
	// <Student, String> spring boot looks at student class and automatically generates sql code
	// targeting students table and map database rows into student java objects
	// springboot looks at your @Id and expects a String input whenever it searches or deletes a student

}
