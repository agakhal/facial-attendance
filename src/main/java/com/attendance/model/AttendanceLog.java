package com.attendance.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;


@Entity // tells this class represents a database table
@Table(name="attendance_logs") // names the database table attendance logs
public class AttendanceLog {
@Id
@GeneratedValue (strategy = GenerationType.IDENTITY) // tells sql to automatically generate a unique id number every time a new row is inserted
private Long id;

private String studentId; // column that stores the id of student who checked in
private LocalDateTime checkInTime; //column that stores the timestamp of when they checked in


public AttendanceLog() {
	
}

public AttendanceLog (String studentId, LocalDateTime checkInTime) {
	this.studentId = studentId;
	this.checkInTime = checkInTime;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getStudentId() {
	return studentId;
}

public void setStudentId(String studentId) {
	this.studentId = studentId;
}

public LocalDateTime getCheckInTime() {
	return checkInTime;
}

public void setCheckInTime(LocalDateTime checkinTime) {
	this.checkInTime = checkinTime;
}

}
