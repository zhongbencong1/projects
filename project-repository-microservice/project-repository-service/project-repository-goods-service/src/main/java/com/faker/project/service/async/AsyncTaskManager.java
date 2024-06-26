package com.faker.project.service.async;

import com.faker.project.constant.AsyncTaskStatusEnum;
import com.faker.project.goods.GoodsInfo;
import com.faker.project.service.IAsyncService;
import com.faker.project.vo.AsyncTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 异步任务执行管理器
 * 对异步任务进行包装管理, 记录并塞入异步任务执行信息
 * */
@Slf4j
@Component
public class AsyncTaskManager {

    /** 异步任务执行信息容器 */
    private final Map<String, AsyncTaskInfo> taskContainer = new HashMap<>(16);

    @Autowired
    private IAsyncService asyncService;

    /** 初始化异步任务 */
    public AsyncTaskInfo initTask() {
        AsyncTaskInfo taskInfo = new AsyncTaskInfo();
        // 设置一个唯一的异步任务 id, 只要唯一即可
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setStatus(AsyncTaskStatusEnum.STARTED);
        taskInfo.setStartTime(new Date());

        // 初始化的时候就要把异步任务执行信息放入到存储容器中
        taskContainer.put(taskInfo.getTaskId(), taskInfo);
        return taskInfo;
    }

    /** 提交异步任务 */
    public AsyncTaskInfo submit(List<GoodsInfo> goodsInfos) {
        // 初始化一个异步任务的监控信息
        AsyncTaskInfo taskInfo = initTask();
        asyncService.asyncImportGoods(goodsInfos, taskInfo.getTaskId());
        return taskInfo;
    }

    /** 设置异步任务执行状态信息 */
    public void setTaskInfo(AsyncTaskInfo taskInfo) {
        taskContainer.put(taskInfo.getTaskId(), taskInfo);
    }

    /** 获取异步任务执行状态信息 */
    public AsyncTaskInfo getTaskInfo(String taskId) {
        return taskContainer.get(taskId);
    }
}
