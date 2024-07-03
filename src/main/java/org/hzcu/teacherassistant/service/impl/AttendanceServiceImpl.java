package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hzcu.teacherassistant.domain.Attendance;
import org.hzcu.teacherassistant.domain.StudentCourses;
import org.hzcu.teacherassistant.mapper.AttendanceMapper;
import org.hzcu.teacherassistant.mapper.StudentCourseMapper;
import org.hzcu.teacherassistant.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {
    @Autowired
    private StudentCourseMapper studentCourseMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;

    @Override
    @Transactional
    public void startAttendance(Integer courseId) {
        List<StudentCourses> studentCourses = studentCourseMapper.selectList(new QueryWrapper<StudentCourses>().eq("course_id", courseId));
        Date currentDate = new Date();

        for (StudentCourses studentCourse : studentCourses) {
            Attendance attendance = new Attendance();
            attendance.setStudentId(studentCourse.getStudentId());
            attendance.setCourseId(courseId);
            attendance.setAttendanceDate(currentDate);
            attendance.setStatus(null); // 初始化状态为空

            this.save(attendance);
        }
    }

    @Override
    public ByteArrayInputStream generateAttendanceReport(int courseId) throws IOException {
        List<Attendance> attendances = attendanceMapper.selectList(new QueryWrapper<Attendance>().eq("course_id", courseId));

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Attendance Report");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Student ID");
            headerRow.createCell(2).setCellValue("Course ID");
            headerRow.createCell(3).setCellValue("Status");
            headerRow.createCell(4).setCellValue("Date");

            // Data rows
            int rowIdx = 1;
            for (Attendance attendance : attendances) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(attendance.getId());
                row.createCell(1).setCellValue(attendance.getStudentId());
                row.createCell(2).setCellValue(attendance.getCourseId());
                row.createCell(3).setCellValue(attendance.getStatus());
                row.createCell(4).setCellValue(attendance.getAttendanceDate().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
