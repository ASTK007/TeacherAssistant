package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.Permissions;
import org.hzcu.teacherassistant.mapper.PermissionsMapper;
import org.hzcu.teacherassistant.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements PermissionService {
}
