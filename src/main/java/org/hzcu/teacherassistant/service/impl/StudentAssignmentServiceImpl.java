package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.StudentAssignments;
import org.hzcu.teacherassistant.mapper.StudentassignmentsMapper;
import org.hzcu.teacherassistant.service.StudentAssignmentService;
import org.springframework.stereotype.Service;

@Service
public class StudentAssignmentServiceImpl extends ServiceImpl<StudentassignmentsMapper, StudentAssignments> implements StudentAssignmentService {
}
