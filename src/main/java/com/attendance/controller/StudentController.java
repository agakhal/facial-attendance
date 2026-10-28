package com.attendance.controller;
import com.attendance.service.FaceService;
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
	 // make sure a photo was sent
if (student.getStudentId() == null || student.getStudentId().isEmpty()) {
	return "Please enter a Student ID.";
}
if (student.getFaceVector() == null || student.getFaceVector().isEmpty()) {
	return "Please capture a photo first.";
}
if (studentRepository.existsById(student.getStudentId())) {
	return "That Student ID is already registered.";
}

// read the face from the photo and save its numbers on the student
try {
	float[] embedding = faceService.extract(student.getFaceVector());
	student.setFaceEmbedding(FaceService.toText(embedding));
	student.setFaceVector(null); // <-- add this line
} catch (Exception e) {
	e.printStackTrace(); // prints the error in the logs
	return "Could not read a face from the photo. Please try again.";
}
	studentRepository.save(student);
	 return "Student " + student.getName() + " signed up successfully!";
	}	
	@Autowired
	private FaceService faceService; // reads faces from photos
	
}
