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

import reactor.core.publisher.Flux;
import reactor.core.publisher.MonoProcessor;
import reactor.util.context.Context;

import java.util.function.Function;

/**
 * @author <a herf="zzz13129180808@gmail.com">a9043</a>
 */
public class WorkflowFactory {
    public static final String REQUEST_KEY = "blog.lohoknang.workflow.reacitve.request";

    public static <T> Flux<Context> create(Flux<T> source, WorkflowDefinition workFlowDefinition) {
        return source
                .map(request -> Context.of(REQUEST_KEY, request))
                .flatMap(context -> workFlowDefinition
                        .getWorkflowDefinitionFlux()
                        .reduce(
                                MonoProcessor.just(context),
                                (contextMono, parallelFlux) -> parallelFlux
                                        .flatMap(contextMono::map)
                                        .reduce(Context::putAll)
                                        .toProcessor()
                        )
                        .flatMap(Function.identity())
                );
    }
}
