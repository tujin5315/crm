<!DOCTYPE html>
<html>
<head>
    <#include "../common.ftl">
</head>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
    <#-- 暂缓数据添加id -->
    <input name="lossId" type="hidden" value="${lossId!}"/>
    <#-- 暂缓数据更新id -->
    <input name="id" type="hidden" value="${(customerRep.id)!}"/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">暂缓措施</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input userName"
                   lay-verify="required" name="measure" id="measure"  value="${(customerRep.measure)!}" placeholder="请输入暂缓措施">
        </div>
    </div>

    <br/>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-lg" lay-submit=""
                    lay-filter="addOrUpdateCustomerRep">确认
            </button>
            <button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
        </div>
    </div>
</form>
<script type="text/javascript" src="${ctx}/js/customerLoss/customer.rep.add.update.js"></script>
</body>
</html>