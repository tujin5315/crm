package com.msbtj.crm.controller;

import com.msbtj.crm.base.BaseController;
import com.msbtj.crm.base.ResultInfo;
import com.msbtj.crm.model.TreeModel;
import com.msbtj.crm.service.ModuleService;
import com.msbtj.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;

    /**
     * 查询所有资源列表
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 进入授权页面
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request){
        // 将得到的id集合放到作用域中 便于授权中的隐藏于获取该id集合
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 查询所有资源  展示在资源页面
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryModules(){
        return moduleService.queryModules();
    }

    /**
     * 进入资源管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "module/module";
    }

    /**
     * 添加资源管理功能
     * @param module
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addModule(Module module){
        moduleService.addModules(module);
        return success("菜单添加成功！");
    }

    /**
     * 修改资源管理功能
     * @param module
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("菜单修改成功！");
    }

    /**
     * 删除资源管理功能
     * @param id  菜单id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModule(id);
        return success("菜单删除成功！");
    }

    /**
     * 进入资源管理添加页面
     * @param grade 层级
     * @param parentId  父级id
     * @return
     */
    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade,Integer parentId,HttpServletRequest request){
        // 需要将层级和父级id传递过去
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }

    /**
     * 打开资源管理更新的页面
     * @param id
     * @return
     */
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id, Model model){
        // 将修改的对象设置到作用域中
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }
}
