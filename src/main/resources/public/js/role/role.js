layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //角色列表展示
    var  tableIns = table.render({
        elem: '#roleList',
        url : ctx+'/role/list',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "roleListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'roleName', title: '角色名', minWidth:50, align:"center"},
            {field: 'roleRemark', title: '角色备注', minWidth:100, align:'center'},
            {field: 'createDate', title: '创建时间', align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间', align:'center',minWidth:150},
            {title: '操作', minWidth:150, templet:'#roleListBar',fixed:"right",align:"center"}
        ]]
    });

    // 多条件搜索
    $(".search_btn").on("click",function () {
        table.reload("roleListTable",{
            page:{
                curr:1
            },
            where:{
                // 角色名
                roleName:$("input[name='roleName']").val()
            }
        })
    });

    // 头工具栏事件
    table.on('toolbar(roles)',function (data) {
        switch (data.event) {
            case "add": // 添加事件
                openAddOrUpdateRoleDialog();
                break;
            case "grant": // 授权操作
                // 获取记录表格中的数据
                var checkStatus = table.checkStatus(data.config.id);
                // 打开授权的对话框
                openADDGrantDialog(checkStatus.data);
                break;
        }
    });

    // 行工具栏事件
    // 删除角色
    table.on('tool(roles)',function (obj) {
        var layEvent =obj.event;
        if(layEvent === "edit"){
            openAddOrUpdateRoleDialog(obj.data.id);
        }else if(layEvent === "del"){
            layer.confirm("确认删除当前记录?",{icon: 3, title: "角色管理"},function (index) {
                $.post(ctx+"/role/delete",{id:obj.data.id},function (data) {
                    if(data.code==200){
                        layer.msg("删除成功");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg);
                    }
                })
            })
        }
    });


    /**
     * 添加/更新操作
     * @param id
     */
    function openAddOrUpdateRoleDialog(id) {
        var title="角色管理-角色添加";
        var url=ctx+"/role/addOrUpdateRolePage";
        if(id){
            title="角色管理-角色更新";
            url=url+"?roleId="+id;
        }
        layui.layer.open({
            title:title,
            type:2,
            area:["400px","450px"],
            maxmin:true,
            content:url
        })
    }

    /**
     * 打开授权页面
     */
    function openADDGrantDialog(data){
        // 首先需要判断是否打开了页面
        if(data.length==0){
            layer.msg("请选择一个角色进行授权",{icon:5});
            return;
        }
        // 一次性只能给一名角色进行授权
        if(data.length>1){
            layer.msg("暂不支持多角色授权",{icon:5});
            return;
        }
        var url = ctx + "/module/toAddGrantPage?roleId="+data[0].id;
        var title = "角色管理 -- 角色授权"
        layui.layer.open({
            title:title,
            content:url,
            area:["600px","400px"],
            maxmin:true,
            type:2
        })

    }



});
