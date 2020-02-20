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
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.util.context.Context;

/**
 * @author <a herf="zzz13129180808@gmail.com">a9043</a>
 */
@Slf4j
class WorkflowDefinitionFactoryTest {
    @Test
    void create() {
        Flux<String> source = Flux.just("videoA", "videoB", "videoC");
        Flux<ParallelFlux<WorkUnit>> workflowDefinitionFlux = Flux.just(
                Flux.<WorkUnit>just(
                        context -> context.put("workUnit1", "workUnit1")
                ).parallel(),
                Flux.<WorkUnit>just(
                        context -> context.put("workUnit2A", "workUnit2A"),
                        context -> context.put("workUnit2B", "workUnit2B"),
                        context -> context.put("workUnit2C", "workUnit2C")
                ).parallel(),
                Flux.<WorkUnit>just(
                        context -> context.put("workUnit3", "workUnit3")
                ).parallel()
        );

        WorkflowDefinition workflowDefinition = new WorkflowDefinition("workflow", workflowDefinitionFlux);

        Flux<Context> contextFlux = WorkflowFactory.create(source, workflowDefinition);

        contextFlux
                .log()
                .subscribe();
    }
}