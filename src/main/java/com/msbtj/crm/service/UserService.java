package com.msbtj.crm.service;

import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.UserMapper;
import com.msbtj.crm.model.UserModel;
import com.msbtj.crm.utils.AssertUtil;
import com.msbtj.crm.utils.Md5Util;
import com.msbtj.crm.utils.UserIDBase64;
import com.msbtj.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userName 用户姓名
     * @param userPwd  用户密码
     */
    public UserModel userLogin(String userName, String userPwd){
       // 检查用户姓名与密码是否为空
        checkUserParmas(userName,userPwd);
        // 调用controller层 查询信息
        User user = userMapper.queryUserByName(userName);
        // 判断用户对象是否为空
        AssertUtil.isTrue(user==null,"该用户不存在");
        // 判断密码是否正确
        checkUserPwd(userPwd,user.getUserPwd());
        // 用户登录 返回给controller层一个userModel对象
        return buildUserInfo(user);
    }

    /**
     * 返回给controller层的用户的信息
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setId(user.getId());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 用来做密码判断
     *  先将密码加密为MD5格式，再与数据库中的密码作比较
     *
     *  密码错误，抛出异常
     * @param userPwd
     * @param upwd
     */
    private void checkUserPwd(String userPwd, String upwd) {
        // 先将密码加密为MD5格式
        userPwd = Md5Util.encode(userPwd);
        // 判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(upwd),"用户名密码不正确");

    }

    /**
     * 用户姓名与用户密码非空校验
     * @param userName
     * @param userPwd
     */
    private void checkUserParmas(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }
    /**
      修改密码
         1.接收四个参数(用户id、原始密码、新密码、修改密码)
         2.通过id查询用户记录，返回用户对象
         3.参数校验
            待更新用户是否存在
            判断原始密码非空且密码是否正确
            判断新密码以及确认密码是否为空
            判断新密码以及确认密码是否和原始密码一致
            判断新密码和确认密码是否一致
         4.设置用户的新密码
             需要将新密码通过指定算法进行加密(md5加密)
         5.执行更新操作 判断受影响的行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPwdByUserId(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        // 通过id查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 待更新用户是否存在
        AssertUtil.isTrue(null==user,"用户不存在");
        // 参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        // 设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        // 执行更新操作 判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户更新失败");


    }

    /**
     * 待更新用户是否存在
     *    判断原始密码非空且密码是否正确
     *    判断新密码以及确认密码是否为空
     *    判断新密码以及确认密码是否和原始密码一致
     *    判断新密码和确认密码是否一致
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */

    public void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        // 判断原始密码非空
        AssertUtil.isTrue(oldPwd==null,"原始密码不能为空");
        // 判断新密码以及确认密码是否为空
        AssertUtil.isTrue(newPwd==null,"新密码不能为空");
        AssertUtil.isTrue(repeatPwd==null,"确认密码不能为空");
        // 判断原始密码是否正确
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确");
        // 判断新密码以及确认密码是否和原始密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"原密码不能与新密码相同");
        // 判断新密码和确认密码是否一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"新密码和确认密码不一致");
    }
}
