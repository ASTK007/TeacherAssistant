package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.Roles;
import org.hzcu.teacherassistant.domain.Users;
import org.hzcu.teacherassistant.mapper.PermissionsMapper;
import org.hzcu.teacherassistant.mapper.RolesMapper;
import org.hzcu.teacherassistant.mapper.UsersMapper;
import org.hzcu.teacherassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper,Users> implements UserService , UserDetailsService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersMapper.findUserByName(username);
        if (user == null){
            throw new UsernameNotFoundException(username);
        }
        List<Roles> roles = rolesMapper.selectRolesByUid(user.getId());

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roles.stream().map(Roles::getRoleName).toArray(String[]::new))
                .build();
    }

    @Override
    public List<Users> getStudent() {
        return usersMapper.selectStudent();
    }
}
