$(function () {
    loadModuleInfo(); 
});


var zTreeObj;

/**
 * 加载资源树形数据
 */
function loadModuleInfo() {
    // 数据
    // 通过ajax查询数据列表
    $.ajax({
        type:"get",
        url:ctx+"/module/queryAllModules",
        data:{
            roleId:$("[name='roleId']").val()
        },
        dataType:"json",
        success:function (data) {
            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            // 配置信息对象  zTree的参数配置
            var setting = {
                // 使用复选框
                check:{
                    enable:true,
                   /* chkboxType: { "Y": "ps",
                        "N": "ps" }*/
                },
               /* view:{
                    showLine: false
                    // showIcon: false
                },*/
                // 使用简单的json数据
                data:{
                    simpleData: {
                        enable:true
                    }
                },
                // 当复选框取消选中或被选中时触发的函数
                callback: {
                    onCheck: zTreeOnCheck
                }
            }
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    })
}


function zTreeOnCheck(event, treeId, treeNode) {
    //alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
    // getCheckNodes 获取所有被勾选的集合 若checked=true 表示所有被勾选的集合，若checked=false  白哦是所有未被勾选的集合
    var nodes= zTreeObj.getCheckedNodes(true);
    // 获取所有的资源的id值
    var mids="mids=";
    // 判断并遍历选中的节点集合
    for(var i=0;i<nodes.length;i++){
        // 不是最后一个 需要拼接
        if(i<nodes.length-1){
            mids+= nodes[i].id+"&mids=";
            // 是最后一个 不需要拼接
        }else{
            mids+= nodes[i].id;
        }
    }
    // 通过ajax传输数据
    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+$("[name='roleId']").val(),
        dataType:"json",
        success:function (data) {
            console.log(data);
        }
    })

}