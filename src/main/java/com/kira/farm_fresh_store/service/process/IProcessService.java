package com.kira.farm_fresh_store.service.process;

public interface IProcessService {
    String approveOrder(String taskId);
    String rejectOrder(String taskId);
}
