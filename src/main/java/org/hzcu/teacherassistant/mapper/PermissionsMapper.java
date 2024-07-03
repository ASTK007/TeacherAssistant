package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hzcu.teacherassistant.domain.Permissions;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {
    int deleteByPrimaryKey(Integer id);

    int insert(Permissions record);

    int insertSelective(Permissions record);

    Permissions selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permissions record);

    int updateByPrimaryKey(Permissions record);

    @Select("select permissions.permission_name from roles_permissions,permissions where rid = #{id} and pid = permissions.id")
    List<Permissions> selectByRid(Integer id);
}