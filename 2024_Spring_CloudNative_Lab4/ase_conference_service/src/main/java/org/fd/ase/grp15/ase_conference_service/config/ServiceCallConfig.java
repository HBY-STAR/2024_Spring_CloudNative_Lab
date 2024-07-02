package org.fd.ase.grp15.ase_conference_service.config;

import jakarta.servlet.http.HttpServletRequest;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@Configuration
public class ServiceCallConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();
        rt.getInterceptors().add((request, body, execution) -> {
            // 本服务收到的request
            HttpServletRequest rq = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            // https://istio.io/latest/zh/docs/tasks/observability/distributed-tracing/overview/
            List<String> headerNamesToCopy = List.of(
                    "x-request-id",
                    "x-b3-traceid",
                    "x-b3-spanid",
                    "x-b3-parentspanid",
                    "x-b3-sampled",
                    "x-b3-flags",
                    "b3",
                    "x-ot-span-context",
                    "traceparent",
                    "tracestate",
                    "x-datadog-trace-id",
                    "x-datadog-parent-id",
                    "x-datadog-sampling-priority",
                    "grpc-trace-bin",
                    "x-cloud-trace-context",
                    "sw8", // for skywalking
                    "Cookie",
                    "satoken",
                    "user-agent"
            );

            for (var hn: headerNamesToCopy) {
                String header = rq.getHeader(hn);
                if (header != null) {
                    request.getHeaders().add(hn, header);
                }
            }

            return execution.execute(request, body);
        });
        return rt;
    }

    @Bean
    public RestTemplateAdapter restTemplateAdapter(RestTemplate restTemplate) {
        return RestTemplateAdapter.create(restTemplate);
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(RestTemplateAdapter restTemplateAdapter) {
        return HttpServiceProxyFactory.builderFor(restTemplateAdapter).build();
    }

    @Bean
    public IUserConferenceRoleService userConferenceRoleService(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(IUserConferenceRoleService.class);
    }
}
