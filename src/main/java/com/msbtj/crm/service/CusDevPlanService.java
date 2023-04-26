package com.msbtj.crm.service;

import com.msbtj.crm.base.BaseService;
import com.msbtj.crm.dao.CusDevPlanMapper;
import com.msbtj.crm.vo.CusDevPlan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,String> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

}
