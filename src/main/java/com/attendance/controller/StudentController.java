package com.attendance.controller;
import com.attendance.model.Student;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.attendance.model.AttendanceLog;

import java.time.LocalDateTime;
import java.util.List;

@RestController // tells springboot that every method will return raw JSON data
@RequestMapping("/api/FaceMetrics") // base url for all endpoints in this class
@CrossOrigin(origins = "*") // allows any web browser or external app to talk to your springboot backend without getting blocked by security rules
// * means give access to everyone
public class StudentController {
	@Autowired
	private StudentRepository studentRepository; //instantiates so i can directly use 
	@Autowired
	private AttendanceRepository attendanceRepository;
	@GetMapping // collects every record from database and hands u back full list
	public List<Student>getAllStudents() { // defines a java method that returns a list containing student objects . getallstudents is name of that java method
		return studentRepository.findAll(); // built in method to retrieve all data  
}
	@PostMapping ("/signup") //accepts new data like id name facial vector and saves it to database
	public String addStudent (@RequestBody Student student) {
		//request body extracts all information and builds a student object from that new information
	 studentRepository.save(student);
	 return "Student " + student.getName() + " signed up successfully!";
	}	
	
	@PostMapping("/checkin") 
	public String checkInStudent (@RequestBody Student incomingData) {
	    List<Student> allStudents = studentRepository.findAll();
	    
	    for (Student s : allStudents) {
	        if (s.getFaceVector() != null && s.getFaceVector().equals(incomingData.getFaceVector())) {
	            // Log attendance upon match using 's' and your correct repository name
	            AttendanceLog log = new AttendanceLog();
	            log.setStudentId(s.getStudentId());
	            log.setCheckInTime(LocalDateTime.now());
	            attendanceRepository.save(log);
	            
	            return s.getName() + " : Attendance marked!";
	        }
	    }
	    return "Face not recognized!";
	}
	@GetMapping("/{id}") //listens for a get request with a value at the end
	public Student getStudentById(@PathVariable String id) { 
		//extracts whatever text is typed in the end and stores it in java variable id
		return studentRepository.findById(id).orElse(null); // runs a targeted lookup query in SQL if no student is found returns null
	}
	@DeleteMapping ("/{id}")  // listen for delete requests that have values in end of url
	public String deleteStudent(@PathVariable String id) { //extracts whatever text is typed in the end and stores it in java variable id
		studentRepository.deleteById(id); //executes a deletion query in SQL
		return "Student with ID" + id + " has been deleted.";
	}
	
	}
	
