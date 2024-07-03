package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hzcu.teacherassistant.domain.StudentAssignments;
@Mapper
public interface StudentassignmentsMapper extends BaseMapper<StudentAssignments> {
    int deleteByPrimaryKey(Integer id);

    int insert(StudentAssignments record);

    int insertSelective(StudentAssignments record);

    StudentAssignments selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StudentAssignments record);

    int updateByPrimaryKey(StudentAssignments record);
}