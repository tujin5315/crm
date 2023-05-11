package com.msbtj.crm.task;

import com.msbtj.crm.service.CustomerService;
import com.msbtj.crm.utils.TaskUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class jobTask {
    // 注入需要定时任务的方法(只需要注入在哪个类中即可)

    // 定义定时任务的方法
    @Resource
    private CustomerService customerService;
    /**
     * 每两秒执行一次
     */
    @Scheduled(cron = "0 0 0 10 * ? ")
    public void job(){
        System.out.println("定时任务开始执行 -->" + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));
        // 调用需要被定时执行的方法
//        CustomerService customerService = (CustomerService) TaskUtils.getBean("customerService");
        customerService.updateCustomerState();
    }

}
