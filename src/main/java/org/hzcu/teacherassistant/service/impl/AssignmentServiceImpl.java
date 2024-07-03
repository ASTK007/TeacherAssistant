package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.Assignments;
import org.hzcu.teacherassistant.mapper.AssignmentsMapper;
import org.hzcu.teacherassistant.service.AssignmentService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentsMapper, Assignments> implements AssignmentService {
}
