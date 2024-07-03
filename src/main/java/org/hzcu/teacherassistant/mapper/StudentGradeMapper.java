package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hzcu.teacherassistant.domain.GradesVO;
import org.hzcu.teacherassistant.domain.StudentGrade;

import java.util.List;

@Mapper
public interface StudentGradeMapper extends BaseMapper<StudentGrade> {

    List<GradesVO> getGradesByStuId(Integer id);
}
