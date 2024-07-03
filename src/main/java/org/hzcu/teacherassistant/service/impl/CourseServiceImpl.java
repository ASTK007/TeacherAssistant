package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.Courses;
import org.hzcu.teacherassistant.mapper.CoursesMapper;
import org.hzcu.teacherassistant.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CoursesMapper, Courses> implements CourseService {
}
