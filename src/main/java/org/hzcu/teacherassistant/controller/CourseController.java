package org.hzcu.teacherassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.hzcu.teacherassistant.domain.Courses;
import org.hzcu.teacherassistant.domain.Resources;
import org.hzcu.teacherassistant.domain.Result;
import org.hzcu.teacherassistant.service.CourseService;
import org.hzcu.teacherassistant.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@Tag(name = "课程管理")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Resource
    private ResourcesService resourcesService;
    @Resource
    private ChatController chatController;

    /**
     * 查询所有课程
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping
    @Operation(summary = "查询所有课程")
        public Result getAllCourse(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10")Integer pageSize) {
        return Result.success(courseService.page(new Page<>(pageNo, pageSize)));
    }

    /**
     * 新增课程
     * @param course
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "新增课程")
    public Result add(@RequestBody Courses course){
        return Result.success(courseService.save(course));
    }

    /**
     * 修改课程
     * @param course
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "修改课程")
    public Result update(@RequestBody Courses course){
        return Result.success(courseService.updateById(course));
    }

    /**
     * 删除课程
     * @param id
     * @return
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "删除课程")
    public Result delete(Integer id){
        return Result.success(courseService.removeById(id));
    }
    /**
     * 查询课程资源
     */
    @GetMapping("/resource")
    @Operation(summary = "查询课程资源")
    public Result ResByCourseId(Integer courseId){
        return Result.success(resourcesService.list(new QueryWrapper<Resources>().eq("course_id", courseId)));
    }
    /**
     * 添加课程资源链接
     */
    @PostMapping("/resource")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "添加课程资源链接")
    public Result uploadCourseResource(@RequestBody Resources resource){
        return Result.success(resourcesService.save(resource));
    }

    /**
     * 删除课程资源
     * @param id
     * @return
     */
    @DeleteMapping("/resource")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "删除课程资源")
    public Result deleteResById(Integer id){
        return Result.success(resourcesService.removeById(id));
    }

    /**
     * 搜索课程资源
     */
    @PostMapping("/search/course/{name}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "网上搜索课程资源")
    public Result searchCourseRes(@PathVariable String name) throws Exception {
        String answer = chatController.askQuestion("请在互联网上搜索关于" + name + "的教学资源，如课程视频、教案、练习题");
        Thread.sleep(200);
        return Result.success(answer);
    }

    /**
     * 生成教学计划
     */
    @PostMapping("/plan/course/{name}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "生成教学计划")
    public Result generateCourseplan(@PathVariable String name) throws Exception {
        String answer = chatController.askQuestion("请生成关于课程" + name + "的教学计划");
        Thread.sleep(200);
        return Result.success(answer);
    }
}
