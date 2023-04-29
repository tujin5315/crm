layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var  formSelects = layui.formSelects;

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前ifreame层的索引
        parent.layer.close(index); // 再执行关闭
    });
/**
 * 加载角色下拉框
 *  1.配置远程搜索，请求头，请求参数，请求类型
 *  formSelects.config(ID,Options,isJson)
 *
 *  id: xm-select的值
 *  options   配置项
 *  isjson    是否传输json数据  true将添加请求头  content-Type:application/json;charset=utf-8
 */
    var userId=$("[name='id']").val();
    formSelects.config('selectId',{
        type:"post", // 请求方式
        searchUrl:ctx+"/role/queryAllRoles?userId="+userId, // 请求地址
        //自定义返回数据中name的key, 默认 name
        keyName: 'roleName', // 下拉框中的文本内容，要求返回的数据与key对应
        //自定义返回数据中value的key, 默认 value
        keyVal: 'id'
    },true);

    /*
    表单提交监听事件
    * */
    form.on("submit(addOrUpdateUser)", function (data) {
        // 加top  顶层打开 多层窗口时需要添加
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        var url=ctx + "/user/add";
        // 根据id信息判断是否是更新用户信息
        if($("[name='id']").val()){
            url=ctx + "/user/update";
        }
        // 得到所有的表单元素
        var formart = data.field;
        $.post(url, formart, function (res) {
            // 判断成功
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    // 关闭所有弹出层页面
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(
                    res.msg, {
                        icon: 5
                    }
                );
            }
        });
        return false;
    });
});