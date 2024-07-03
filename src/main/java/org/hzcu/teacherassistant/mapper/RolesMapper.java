package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hzcu.teacherassistant.domain.Roles;

import java.util.List;

@Mapper
public interface RolesMapper extends BaseMapper<Roles> {
    int deleteByPrimaryKey(Integer id);

    int insert(Roles record);

    int insertSelective(Roles record);

    Roles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Roles record);

    int updateByPrimaryKey(Roles record);

    List<Roles> selectRolesByUid(Integer uid);
}