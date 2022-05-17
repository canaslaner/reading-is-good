package com.getir.rig.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class ProblemConfig {

    @Autowired
    public ProblemConfig(ObjectMapper jacksonObjectMapper) {
        jacksonObjectMapper.registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }
}
