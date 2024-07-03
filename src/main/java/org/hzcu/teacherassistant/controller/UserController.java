package org.hzcu.teacherassistant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hzcu.teacherassistant.domain.GradesVO;
import org.hzcu.teacherassistant.domain.Result;
import org.hzcu.teacherassistant.domain.StudentGrade;
import org.hzcu.teacherassistant.service.StudentGradeService;
import org.hzcu.teacherassistant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentGradeService studentGradeService;
    @Autowired
    private ChatController chatController;

    /**
     * 查询所有学生
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "查询所有学生")
    public Result getStudents() {
        return Result.success(userService.getStudent());
    }

    /**
     * 添加学生课程成绩
     * @param studentGrade
     * @return
     */
    @PostMapping("/grade")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "添加学生课程成绩")
    public Result addGrade(@RequestBody StudentGrade studentGrade) {
        return Result.success(studentGradeService.save(studentGrade));
    }

    /**
     * 修改学生课程成绩
     * @param studentGrade
     * @return
     */
    @PutMapping("/grade")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "修改学生课程成绩")
    public Result updateGrade(@RequestBody StudentGrade studentGrade) {
        return Result.success(studentGradeService.updateById(studentGrade));
    }

    /**
     * 删除学生课程成绩
     * @param id
     * @return
     */
    @DeleteMapping("/grade/{id}")
    @PreAuthorize("hasAnyRole('teacher')")
    @Operation(summary = "删除学生课程成绩")
    public Result deleteGrade(@PathVariable Integer id) {
        return Result.success(studentGradeService.removeById(id));
    }

    /**
     * 生成学生成绩分析报告
     */
    @GetMapping("/grade/student/{id}")
    @Operation(summary = "生成学生成绩分析报告")
    public Result getGradesById(@PathVariable Integer id) throws Exception {
        List<GradesVO> stuGrades = studentGradeService.getGradesByStuId(id);
        String answer = chatController.askQuestion("这是一位同学的成绩:"+stuGrades.toString()+"请根据数据生成一份成绩分析报告");
        return Result.success(answer);
    }
}
