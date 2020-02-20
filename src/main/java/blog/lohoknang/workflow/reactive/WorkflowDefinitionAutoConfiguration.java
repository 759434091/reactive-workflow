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

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a herf="zzz13129180808@gmail.com">a9043</a>
 */
@Configuration
@ConditionalOnProperty(value = "blog.lohoknang.workflow.reactive.enabled", havingValue = "true")
@ConfigurationProperties("blog.lohoknang.workflow.reactive")
public class WorkflowDefinitionAutoConfiguration implements ApplicationContextAware {
    private GenericApplicationContext applicationContext;

    @Getter
    @Setter
    private Map<String, List<List<String>>> definitions;

    @PostConstruct
    public void postConstruct() {
        Objects.requireNonNull(definitions, "Invalid workflow definition");
        Map<String, WorkUnit> workUnitMap = applicationContext.getBeansOfType(WorkUnit.class);

        definitions
                .entrySet()
                .stream()
                .map(entry -> new WorkflowDefinition(
                        entry.getKey(),
                        Flux
                                .fromIterable(entry
                                        .getValue()
                                        .stream()
                                        .map(list -> Flux
                                                .fromIterable(list
                                                        .stream()
                                                        .map(workUnitMap::get)
                                                        .collect(Collectors.toList())
                                                )
                                                .parallel())
                                        .collect(Collectors.toList()))
                ))
                .forEach(workflowDefinition -> applicationContext.registerBean(
                        workflowDefinition.getName(),
                        WorkflowDefinition.class,
                        () -> workflowDefinition
                ));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }
}
