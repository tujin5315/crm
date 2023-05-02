layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    /**
     * 表单提交
     */
    form.on('submit(addOrUpdateCustomer)',function (data) {
        var index= top.layer.msg("数据提交中,请稍后...",{icon:16,time:false,shade:0.8});
        var url = ctx+"/customer/add";
        // 获取隐藏域中的id
        if($("[name='id']").val()){
            url=ctx+"/customer/update";
        }
        $.post(url,data.field,function (res) {
            if(res.code==200){
                top.layer.msg("操作成功");
                top.layer.close(index);
                layer.closeAll("iframe");
                // 刷新父页面
                parent.location.reload();
            }else{
                layer.msg(res.msg);
            }
        });
        return false;
    });
    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        // 当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); // 先得到当前ifreame层的索引
        parent.layer.close(index); // 再执行关闭
    });

});