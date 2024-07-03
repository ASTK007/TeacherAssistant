package org.hzcu.teacherassistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hzcu.teacherassistant.domain.Users;

import java.util.List;

public interface UserService extends IService<Users> {
    List<Users> getStudent();
}
