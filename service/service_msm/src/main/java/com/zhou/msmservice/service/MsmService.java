package com.zhou.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean sendMsm(String phoneNumber, Map<String, Object> codeMap);
}
