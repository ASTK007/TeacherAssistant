package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hzcu.teacherassistant.domain.Assignments;
@Mapper
public interface AssignmentsMapper extends BaseMapper<Assignments> {
    int deleteByPrimaryKey(Integer id);

    int insert(Assignments record);

    int insertSelective(Assignments record);

    Assignments selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Assignments record);

    int updateByPrimaryKey(Assignments record);
}