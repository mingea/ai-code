package com.wzb.aicode.controller;

import com.wzb.aicode.common.BaseResponse;
import com.wzb.aicode.common.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wzb
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @RequestMapping("/")
    public BaseResponse<String> check() {
        return ResultUtils.success("ok");
    }
}
