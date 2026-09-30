package com.attendance.repository;

import com.attendance.model.AttendanceLog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceLog, Long> {
List<AttendanceLog> findByStudentId(String studentId);
}