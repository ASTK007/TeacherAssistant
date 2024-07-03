package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.GradesVO;
import org.hzcu.teacherassistant.domain.StudentGrade;
import org.hzcu.teacherassistant.mapper.StudentGradeMapper;
import org.hzcu.teacherassistant.service.StudentGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentGradeServiceImpl extends ServiceImpl<StudentGradeMapper, StudentGrade> implements StudentGradeService {
    @Autowired
    private StudentGradeMapper studentGradeMapper;
    @Override
    public List<GradesVO> getGradesByStuId(Integer id) {
        return studentGradeMapper.getGradesByStuId(id);
    }
}
