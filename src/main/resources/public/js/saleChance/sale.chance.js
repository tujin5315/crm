layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //用户列表展示
    var  tableIns = table.render({
        // 容器的id属性值
        elem: '#saleChanceList',
        // 访问数据的URL(后台的数据接口)
        url : ctx+'/sale_chance/list',
        // 单元格最小宽度
        cellMinWidth : 95,
        // 开启分页
        page : true,
        // 容器的高度 full-差值
        height : "full-125",
        limits : [10,15,20,25],
        // 默认显示10页
        limit : 10,
        // 开启头部工具栏
        toolbar: "#toolbarDemo",
        id : "saleChanceListTable",
        cols : [[ // 表头
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'uname', title: '指派人', align:'center'},
            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d){
                    return formatterState(d.state);
                }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    function formatterState(state){
        if(state==0){
            return "<div style='color:yellow '>未分配</div>";
        }else if(state==1){
            return "<div style='color: green'>已分配</div>";
        }else{
            return "<div style='color: red'>未知</div>";
        }
    }

    function formatterDevResult(value){
        /**
         * 0-未开发
         * 1-开发中
         * 2-开发成功
         * 3-开发失败
         */
        if(value==0){
            return "<div style='color: yellow'>未开发</div>";
        }else if(value==1){
            return "<div style='color: #00FF00;'>开发中</div>";
        }else if(value==2){
            return "<div style='color: #00B83F'>开发成功</div>";
        }else if(value==3){
            return "<div style='color: red'>开发失败</div>";
        }else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

    // 多条件搜索
    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").on("click",function(){
        /**
         * 表格重载
         *    多条件查询
         */
        tableIns.reload({
            page: {
                curr: 1 //重新从第 1 页开始
            },// 需要传递给后端的参数
            where: { // 设定异步数据接口的额外参数，任意设
                customerName: $("input[name='customerName']").val(),// 客户名
                createMan: $("input[name='createMan']").val(),// 创建人
                state: $("#state").val()    //分配状态
            }
        })
    });

    // 监听头部工具栏事件
    table.on('toolbar(saleChances)', function(data){
        var checkStatus = table.checkStatus("saleChanceListTable");
        // obj.event:对应元素上设置的lay-event属性值
        switch(data.event){
            case "add":
                // 添加操作
                openAddOrUpdateSaleChanceDialog();
                break;
            case "del":
                // 删除操作
                delSaleChance(data);
                break;
        };
    });


    /**
     * 行工具栏监听事件
     *
     */
    /**
     * 删除单条记录
     */
    table.on("tool(saleChances)", function(data){
        var layEvent = data.event;
        if(layEvent === "edit") { // 编辑操作
            var saleChanceId = data.data.id;
            openAddOrUpdateSaleChanceDialog(saleChanceId);
        }else if(layEvent === "del") { // 删除操作
            layer.confirm('确定删除当前数据？', {icon: 3, title: "机会数据管理"}, function (index) {
                // 关闭确认框
                layer.close(index);
                $.post(ctx+"/sale_chance/delete",{ids:data.data.id},function (result) {
                    if(result.code==200){
                        layer.msg("操作成功！");
                        tableIns.reload();
                    }else{
                        layer.msg(result.msg, {icon: 5});
                    }
                });
            })
        }
    });



    /**
     * 打开添加/修改机会数据页面
     *    如果营销机会为空，则为添加操作
     *    如果营销机会不为空，则为修改操作
     * @param id
     */
    function openAddOrUpdateSaleChanceDialog(id){
        console.log(id);
        var url  =  ctx+"/sale_chance/toSaleChancePage";
        var title="营销机会管理-机会添加";
        if(id){
            url = url+"?id="+id;
            title="营销机会管理-机会更新";
        }
        // iframe层  type=2
        layui.layer.open({
            // 标题
            title : title,
            // 类型
            type : 2,
            // 宽高
            area:["500px","620px"],
            // 最大化最小化
            maxmin:true,
            // url地址
            content : url
        });
    }


    /**
     * 批量删除
     * @param datas
     */
    function delSaleChance(data) {
        var checkStatus = table.checkStatus("saleChanceListTable");
        // 获取所有被选中的记录对应的数据
        var saleChanceData = checkStatus.data;
        // 判断用户是否选择的记录(选中行的数量大于0)
        if(saleChanceData.length<1){
            layer.msg("请选择删除记录!", {icon: 5});
            return;
        }
        // 询问用户是否确认删除
        layer.confirm('确定删除选中的机会数据？', {icon:3,title:'营销机会管理',
            btn: ['确定','取消'] //按钮
        }, function(index){
            // 关闭确认框
            layer.close(index);
            //  传递数组 格式: ids=1&ids=2&ids=3..
            var ids= "ids=";
            for(var i=0;i<saleChanceData.length;i++){
                if(i<datas.length-1){
                    ids=ids+saleChanceData[i].id+"&ids=";
                }else {
                    ids=ids+saleChanceData[i].id
                }
            }
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/delete",
                data:ids,
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        layer.msg("操作成功！");
                        tableIns.reload();
                    }else{
                        layer.msg(data.msg, {icon: 5});
                    }
                }
            })
        });
    }
});
