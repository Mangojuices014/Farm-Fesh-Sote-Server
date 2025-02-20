package com.kira.farm_fresh_store.service.process;

import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.utils.Util;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessService implements IProcessService {

    private final RuntimeService runtimeService;

    private final Util util;
    private final TaskService taskService;

    @Override
    public String approveOrder(String taskId) {
        if (taskId == null || taskId.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy taskId");
        }
        taskService.complete(taskId);
        return "Đã duyệt đơn hàng với taskId là " + taskId;
    }
}
