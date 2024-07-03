package org.hzcu.teacherassistant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hzcu.teacherassistant.domain.Assignments;
import org.hzcu.teacherassistant.domain.Result;
import org.hzcu.teacherassistant.domain.StudentAssignments;
import org.hzcu.teacherassistant.service.AssignmentService;
import org.hzcu.teacherassistant.service.StudentAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignment")
@Tag(name = "作业管理")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private StudentAssignmentService studentAssignmentService;

    /**
     * 根据课程ID查看对应作业
     * @param courseId
     * @return
     */
    @GetMapping("/course/{courseId}")
    @Operation(summary = "根据课程ID查看对应作业")
    public Result selectAssignmentsByCourseId(@PathVariable Integer courseId) {
        return Result.success(assignmentService.list(new QueryWrapper<Assignments>().eq("course_id", courseId)));
    }

    /**
     * 发布作业
     * @param assignment
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "发布作业")
    public Result add(@RequestBody Assignments assignment) {
        assignmentService.save(assignment);
        return Result.success();
    }

    /**
     * 修改作业
     * @param assignment
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "修改作业")
    public Result update(@RequestBody Assignments assignment) {
        assignmentService.updateById(assignment);
        return Result.success();
    }

    /**
     * 删除作业
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "删除作业")
    public Result delete(@PathVariable Integer id) {
        assignmentService.removeById(id);
        return Result.success();
    }

    /**
     * 学生提交作业
     * @param studentAssignments
     * @return
     */
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('student')")
    @Operation(summary = "学生提交作业")
    public Result submit(@RequestBody StudentAssignments studentAssignments) {
        studentAssignments.setGrade(null);
        studentAssignmentService.save(studentAssignments);
        return Result.success();
    }

}
