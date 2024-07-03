package com.mapper;

import com.domain.Courses;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoursesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Courses record);

    int insertSelective(Courses record);

    Courses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Courses record);

    int updateByPrimaryKey(Courses record);
}