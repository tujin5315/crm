layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前ifreame层的索引
        parent.layer.close(index); // 再执行关闭
    });
    /**
     * 加载指派人的下拉框
     */
    $.ajax({
        type:"get",
        url:ctx+"/user/queryAllSales",
        data:{},
        success:function (data) {
            // console.log(data)
            // 判断返回的数据是否为空
            if(data!=null){
                // 获取隐藏域设置的指派人ID
                var assignManId = $("#assignManId").val();
                // 遍历返回的数据
                for (var i = 0; i < data.length; i++) {
                    var opt = '';
                    // 如果循环得到的ID与隐藏域的ID相等，则表示被选中
                    if(assignManId == data[i].id){
                        // 设置下拉选项 下拉选中
                        opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    }
                    // 设置下拉选项
                    opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>"

                    /*if($("[name='assignMan']").val() == data[i].id ){
                        $("#assignMan").append("<option value=\"" + data[i].id + "\" selected='selected' >" + data[i].uname + "</option>");
                    }else {
                        $("#assignMan").append("<option value=\"" + data[i].id + "\">" + data[i].uname + "</option>");
                    }*/
                    // 将下拉项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            //重新渲染
            layui.form.render("select");
    }});

    /**
     * 监听表单submit事件
     *    form.on(submit(按钮元素的lay-filter属性值),function(data))
     */
    form.on("submit(addOrUpdateSaleChance)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        // 发送ajax请求
        var url=ctx + "/sale_chance/add";
        // 通过营销机会的id来判断当前需要执行添加操作还是修改操作
        // 如果营销机会的id为空，则表示执行添加操作；如果id不为空，则执行更新操作
        // 通过获取隐藏域中的id
        var saleChanceId = $("[name='id']").val();
        // 判断id是否为空
        if(saleChanceId != null && saleChanceId != ''){
            url=ctx + "/sale_chance/update";
        }

        // ajax post请求
        $.post(url, data.field, function (data) {
            if (data.code == 200) { // 成功
                setTimeout(function () {
                    // 关闭加载层
                    top.layer.close(index);
                    // 提示成功
                    top.layer.msg("操作成功！");
                    // 关闭弹出层
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else { // 失败
                layer.msg(
                    res.msg, {
                        icon: 5
                    }
                );
            }
        });
        // 阻止表单提交
        return false;
    });
});