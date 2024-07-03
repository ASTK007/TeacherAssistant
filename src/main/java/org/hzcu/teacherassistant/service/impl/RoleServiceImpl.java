package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.Roles;
import org.hzcu.teacherassistant.mapper.RolesMapper;
import org.hzcu.teacherassistant.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RolesMapper, Roles> implements RoleService {
}
