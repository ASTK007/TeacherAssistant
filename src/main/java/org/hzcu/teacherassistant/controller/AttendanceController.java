package org.hzcu.teacherassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hzcu.teacherassistant.domain.Attendance;
import org.hzcu.teacherassistant.domain.Result;
import org.hzcu.teacherassistant.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/attendance")
@Tag(name = "考勤管理")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    /**
     * 查看课程对应考勤记录
     *
     * @param id
     * @return
     */
    @GetMapping("/course/{id}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "查看课程对应考勤记录")
    public Result getAttendanceByCourseId(@PathVariable int id) {
        return Result.success(attendanceService.list(new QueryWrapper<Attendance>().eq("course_id", id)));
    }

    /**
     * 开始考勤
     *
     * @return
     */
    @PostMapping("/start/course/{id}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "开始考勤")
    public Result startAttendance(@PathVariable int id) {
        attendanceService.startAttendance(id);
        return Result.success();
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "开始考勤")
    public Result add(@RequestBody Attendance attendance) {
        return Result.success(attendanceService.save(attendance));
    }

    /**
     * 修改考勤
     *
     * @param attendance
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "修改考勤")
    public Result modifyAttendance(@RequestBody Attendance attendance) {
        return Result.success(attendanceService.updateById(attendance));
    }

    /**
     * 删除考勤
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "删除考勤")
    public Result deleteAttendance(@PathVariable Integer id) {
        return Result.success(attendanceService.removeById(id));
    }

    /**
     * 生成考勤报表
     * @param courseId
     * @return
     */
    @GetMapping("/report/{courseId}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "生成考勤报表")
    public ResponseEntity<byte[]> generateAttendanceReport(@PathVariable int courseId) {
        try {
            ByteArrayInputStream in = attendanceService.generateAttendanceReport(courseId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=attendance_report.xlsx");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(in.readAllBytes());
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
