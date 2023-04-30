package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.query.RoleQuery;
import com.msbtj.crm.service.RoleService;
import com.msbtj.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    /**
     * 查找所有的角色
     * @return
     */
    @ResponseBody
    @RequestMapping ("queryAllRoles")
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }


    /**
     * 进入role页面
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 分页多条件查询
     *  BaseService层里已经有分页多条件查询的方法，可以直接使用该方法
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> queryByParamsForTable(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 添加角色
     */
    @ResponseBody
    @PostMapping("add")
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功！");
    }

    /**
     * 更新角色
     */
    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色更新成功!");
    }

    /**
     * 进入更新角色的页面(添加/修改角色的弹出框)
     */
    @GetMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer roleId, HttpServletRequest request){
        // 若id不为空，则是更新操作  通过id查询角色记录，保存到作用域中
        if(roleId != null){
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role",role);
        }
        return "role/add_update";
    }
    /**
     * 删除单条角色记录
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("删除角色成功");
    }

    /**
     * 角色授权
     * @param roleId  角色id
     * @param mids   资源id
     * @return
     */
    @ResponseBody
    @PostMapping("addGrant")
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        return success("角色授权成功");
    }


}
