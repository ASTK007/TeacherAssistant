package org.hzcu.teacherassistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hzcu.teacherassistant.domain.Resources;
import org.hzcu.teacherassistant.mapper.ResourcesMapper;
import org.hzcu.teacherassistant.service.ResourcesService;
import org.springframework.stereotype.Service;

@Service
public class ResourcesServiceImpl extends ServiceImpl<ResourcesMapper, Resources> implements ResourcesService {
}
