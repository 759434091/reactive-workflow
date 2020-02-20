/*
 * Copyright (c) 2020 XUENENG LU. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package blog.lohoknang.workflow.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author <a herf="zzz13129180808@gmail.com">a9043</a>
 */
@Slf4j
@SpringBootApplication
@SpringBootTest(classes = WorkflowDefinitionAutoConfigurationTest.class)
@ActiveProfiles("test")
@Import({WorkflowDefinitionAutoConfigurationTest.TestConfig.class})
class WorkflowDefinitionAutoConfigurationTest {
    public static void main(String[] args) {
        SpringApplication.run(WorkflowDefinitionAutoConfigurationTest.class, args);
    }

    @Resource
    private WorkflowDefinition workflow1;

    @Resource
    private WorkflowDefinition workflow2;

    @Test
    public void test() {
        WorkflowFactory.create(Flux.just("video1", "video2"), workflow1).log().subscribe();
        WorkflowFactory.create(Flux.just("video1", "video2"), workflow2).log().subscribe();
    }

    @Configuration
    static class TestConfig {
        @Bean
        public WorkUnit workUnit1() {
            return context -> context.put("workUnit1", "workUnit1");
        }

        @Bean
        public WorkUnit workUnit2() {
            return context -> context.put("workUnit2", "workUnit2");
        }

        @Bean
        public WorkUnit workUnit3A() {
            return context -> context.put("workUnit3A", "workUnit3A");
        }

        @Bean
        public WorkUnit workUnit3B() {
            return context -> context.put("workUnit3B", "workUnit3B");
        }

        @Bean
        public WorkUnit workUnit3C() {
            return context -> context.put("workUnit3C", "workUnit3C");
        }
    }
}