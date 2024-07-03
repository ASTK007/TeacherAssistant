package org.hzcu.teacherassistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hzcu.teacherassistant.domain.GradesVO;
import org.hzcu.teacherassistant.domain.StudentGrade;

import java.util.List;

public interface StudentGradeService extends IService<StudentGrade> {
    List<GradesVO> getGradesByStuId(Integer id);
}
