package com.kira.farm_fresh_store.service.process;

import com.kira.farm_fresh_store.utils.Util;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessService implements IProcessService {

    private final RuntimeService runtimeService;

    private final Util util;

    @Override
    public String processStart() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1uxj2sj", util.generateRandomID());
        return processInstance.getBusinessKey();
    }
}
