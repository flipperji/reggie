package com.huifu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huifu.reggie.common.R;
import com.huifu.reggie.entity.Employee;
import com.huifu.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //获取md5值
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //拿username去库里查找
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employService.getOne(queryWrapper);
        if (one == null) {
            return R.error("登录失败,用户不存在");
        }
        if (!password.endsWith(one.getPassword())) {
            return R.error("密码错误");
        }
        //比对成功，查看员工状态,0为禁用状态
        if (one.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //登录成功,将员工的id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        String md5DigestAsHex = DigestUtils.md5DigestAsHex("123456".getBytes());
        //需默认给一个初始密码
        employee.setPassword(md5DigestAsHex);
        //创建时间
       // employee.setCreateTime(LocalDateTime.now());
        //更新时间
      //  employee.setUpdateTime(LocalDateTime.now());
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);
        //注意username不可重复，会抛异常
        employService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employService.updateById(employee);
        return R.success("更新成功");
    }

    /**
     * 根据id查询员工信息
     * PathVariable 适合拼接的参数查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employService.getById(id);
        if (null == employee) {
            return R.error("未查询到对应员工信息");
        }
        return R.success(employee);
    }

}
