package org.hzcu.teacherassistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hzcu.teacherassistant.domain.Attendance;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface AttendanceService extends IService<Attendance> {
    void startAttendance(Integer courseId);

    ByteArrayInputStream generateAttendanceReport(int courseId) throws IOException;
}
