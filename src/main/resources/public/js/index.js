layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    // 进行登录操作
    form.on('submit(login)', function (data) {
        console.log(data.field); // 当前容器的全部表单字段，名值对形式 name:value
        /* 表单元素的非空校验 */
        /*data = data.field;
        if ( data.username =="undefined" || data.username =="" || data.username.trim()=="") {
            layer.msg('用户名不能为空');
            return false;
        }
        if ( data.password =="undefined" || data.password =="" || data.password.trim()=="")  {
            layer.msg('密码不能为空');
            return false;
        }*/
        /* 发送ajax请求，传递用户姓名与密码，执行用户登录操作 */
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:data.field.username,
                userPwd:data.field.password
            },
            dataType:"json",
            success:function (data) { // result是回调函数，用来接收后端返回的数据
                // 判断是否成功
                if(data.code==200){
                    // 登录成功
                    /**
                     * 设置用户登录状态
                     *  1.利用session会话
                     *    保存用户信息，如果会话存在，则用户是登录状态，否则是非登录状态
                     *    缺点：服务器关闭或浏览器关闭，会话则会消失
                     *  2.利用cookie
                     *    保存用户信息
                     */
                    layer.msg('登录成功', function () {
                        var result =data.result;
                        // 如果点击记住我 设置cookie 过期时间7天
                        if($("#rememberMe").prop('checked')){
                            // 写入cookie 7天
                            $.cookie("userIdStr",result.userIdStr, { expires: 7 });
                            $.cookie("userName",result.userName, { expires: 7 });
                            $.cookie("trueName",result.trueName, { expires: 7 });
                        }
                        else {
                            // 将用户信息设置到cookie中
                            $.cookie("userIdStr",result.userIdStr);
                            $.cookie("userName",result.userName);
                            $.cookie("trueName",result.trueName);
                        }
                        window.location.href=ctx+"/main";
                    });
                }else{
                    // 登录失败
                    layer.msg(data.msg,{icon:5})
                }
            }
        });
        return false;
    });
});