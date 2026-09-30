package com.attendance.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
@Entity // tells springboot and hibernate that this class represents a database table row
@Table(name = "students") // specifies the exact name of the table in MySQL (students)
public class Student {
@Id
private String studentId; // @Id only attaches to studentId
private String name;// does not get @Id because 2 names can be same but 2 studentIds cannot be the same
// do not put vector here bc it is just measurements not an assigned id like social security number or id

@Column (columnDefinition = "TEXT") // setup database column for the variable below me as a TEXT field
private String faceVector; // names the SQL column faceVector 

public Student() {}

public Student (String studentId, String name, String faceVector) {
	this.studentId = studentId;
	this.name = name;
	this.faceVector = faceVector;
}

public String getStudentId() {
	return studentId;
}

public void setStudentId(String studentId) {
	this.studentId = studentId;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getFaceVector() {
	return faceVector;
}

public void setFaceVector(String faceVector) {
	this.faceVector = faceVector;
}




// this class maps to database table using SQL 
//represents single row 
// handles data transfer, when html frontend sends JSON data
// to springboot backend. Spring automatically converts JSON into
// student java object so it can be saved to SQL. 
// without this springboot wouldnt know how to package student data in java


}
