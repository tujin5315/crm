layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    //用户列表展示
    var  tableIns = table.render({
        elem: '#saleChanceList',
        url : ctx+'/sale_chance/list?flag=1',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceListTable",
        cols : [[
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
            {field: 'updateDate', title: '修改时间', align:'center'},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
                }},
            {title: '操作',fixed:"right",align:"center", minWidth:150,templet:"#op"}
        ]]
    });

    /**
     * 开发状态
     *   0=未开发
     *   1=开发中
     *   2=开发成功
     *   3=开发失败
     * @param value
     * @returns {string}
     */

    function formatterDevResult(value){
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
    $(".search_btn").on("click",function(){
        table.reload("saleChanceListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                customerName: $("input[name='customerName']").val(),  //客户名
                createMan: $("input[name='createMan']").val(), //创建人
                devResult:$("#devResult").val()  //开发状态
            }
        })
    });


    /**
     * 行工具栏监听
     */
    table.on("tool(saleChances)", function(data){
        var layEvent = data.event;
        // 判断
        if(layEvent === "dev") { // 开发
            // 打开计划项开发页面
            openCusDevPlanDialog("计划项数据维护",data.data.id);
        }else if(layEvent === "info") { // 详情
            // 打开计划项详情页面
            openCusDevPlanDialog("计划项数据详情",data.data.id);
        }

    });


    // 打开开发计划对话框
    function openCusDevPlanDialog(title,id){
        // iframe层
        layui.layer.open({
            // 标题
            title : title,
            // 图层类型
            type : 2,
            // 宽高
            area:["750px","550px"],
            // 最大最小化
            maxmin:true,
            // 链接地址
            content : ctx+"/cus_dev_plan/toCusDevPlanDataPage?id="+id
        });
    }




});
