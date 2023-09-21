package com.springbatch.batch.controller;

import com.springbatch.batch.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/start")
    public String startJob(@RequestParam(name = "jobName") String jobName) throws Exception {
        jobService.invokeJob(jobName);
        return "Job Started...";
    }

}
