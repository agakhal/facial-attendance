package com.attendance.controller;
import com.attendance.model.AttendanceLog;
import com.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.time.Duration;

@RestController // tells springboot that every method will return raw JSON data
@RequestMapping("/api/attendance") //base url
@CrossOrigin (origins = "*") // allows any web browser or external app to talk to your springboot backend without getting blocked by security rules
//* means give access to everyone
public class AttendanceController{

	@Autowired
	private AttendanceRepository attendanceRepository; // instantiates automatically 
	
	@GetMapping("/clear-db")
	@ResponseBody
	public String clearDatabase() {
	    attendanceRepository.deleteAll();
	    return "Database cleared successfully!";
	}
	
	// Add this helper DTO class inside AttendanceController
    public static class AttendanceRequestDTO {
        private String studentId;
        private String image;

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        
        public String getImage() { return image; }
        public void setImage(String image) { this.image = image; }
    }
    
	//Record a check-in
	@PostMapping("/checkin")// listens for requests sent to checkin
	public String markAttendance (@RequestBody AttendanceRequestDTO request) {
	    String studentId = request.getStudentId();
	    
		//declares markAttendance method that returns a saved AttendanceLog object
		// extracts studentID passed in url and assigns it to studentID variable
		LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Vancouver"));
		List<AttendanceLog> logs = attendanceRepository.findByStudentId(studentId);
	// creates a list named log that holds multiple attendancelog objects and get all existing logs from database 
		
	for (AttendanceLog record: logs) { 
		// for every attendance log item inside my logs list called it record and execute code after {
		LocalDateTime previousTime = record.getCheckInTime();
		// check if student id on this log entry matches the student id which is trying to check in
			// if the ids match, pulls saved check in time and stores it in previous time variable
		long minDiff = Duration.between(previousTime, now).toMinutes();
		//calculates total number of minutes elapsed between when student last checked in
		// and the current time and stores that number as a int value in minDiff variable
			if (minDiff < 5 && minDiff>= 0) {
				return "Already checked in!";
			}
	}
	
	// if no check in was found then check in new student.
	AttendanceLog log = new AttendanceLog(studentId, now);
	// creates a new java object combining the two pieces of data
	attendanceRepository.save(log); // sends object to sql executing insert statement bts
	return "checked in successfully!";
	}
	
	//get all attendance logs
	@GetMapping("/records") // listens for get requests
	public List<AttendanceLog> getAllLogs() {// declares a public method named getAllLogs that returns a list of all check in records from database
	return attendanceRepository.findAll();
	}
}