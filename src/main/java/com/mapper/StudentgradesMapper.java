package com.mapper;

import com.domain.Studentgrades;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentgradesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Studentgrades record);

    int insertSelective(Studentgrades record);

    Studentgrades selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Studentgrades record);

    int updateByPrimaryKey(Studentgrades record);
}