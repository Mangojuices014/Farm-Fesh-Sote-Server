package com.kira.farm_fresh_store.service.process;

import com.kira.farm_fresh_store.exception.ResourceNotFoundException;
import com.kira.farm_fresh_store.utils.Util;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessService implements IProcessService {

    private final RuntimeService runtimeService;

    private final Util util;
    private final TaskService taskService;

    @Override
    public String approveOrder(String taskId) {
        boolean requiresManualConfirmation = true;
        Map<String, Object> variables = new HashMap<>();
        if (taskId == null || taskId.isEmpty()) {
            requiresManualConfirmation = false;
        }
        variables.put("requiresManualConfirmation", requiresManualConfirmation);

        // Truyền biến vào complete
        taskService.complete(taskId, variables);

        return "Đã duyệt đơn hàng với taskId là " + taskId;
    }

    @Override
    public String rejectOrder(String taskId) {
        boolean requiresManualConfirmation = false;
        Map<String, Object> variables = new HashMap<>();
        if (taskId == null || taskId.isEmpty()) {
            requiresManualConfirmation = true;
        }
        variables.put("requiresManualConfirmation", requiresManualConfirmation);

        // Truyền biến vào complete
        taskService.complete(taskId, variables);

        return "Đã hủy đơn hàng với taskId là " + taskId;
    }

}
