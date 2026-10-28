package com.attendance.controller;
import com.attendance.model.Student;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.attendance.model.AttendanceLog;
import java.time.ZoneId;

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
	
	@PostMapping ("/signup") //accepts new data like id name facial vector and saves it to database
	public String addStudent (@RequestBody Student student) {
		//request body extracts all information and builds a student object from that new information
	 studentRepository.save(student);
	 return "Student " + student.getName() + " signed up successfully!";
	}	
	
}
