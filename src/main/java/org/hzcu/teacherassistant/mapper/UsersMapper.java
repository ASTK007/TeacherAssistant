package org.hzcu.teacherassistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hzcu.teacherassistant.domain.Users;

import java.util.List;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    @Select("SELECT users.id ,username, email, `name`, gender FROM users ,roles_user WHERE users.id=uid AND rid=1")
    List<Users> selectStudent();

    @Select("select * from users where username = #{username}")
    Users findUserByName(String username);
}