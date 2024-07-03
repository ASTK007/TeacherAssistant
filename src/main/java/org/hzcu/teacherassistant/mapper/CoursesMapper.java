package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hzcu.teacherassistant.domain.Courses;
@Mapper
public interface CoursesMapper extends BaseMapper<Courses> {
    int deleteByPrimaryKey(Integer id);

    int insert(Courses record);

    int insertSelective(Courses record);

    Courses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Courses record);

    int updateByPrimaryKey(Courses record);
}