package demosecurity.utilities;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Service
public class TemplateRendererService {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public TemplateRendererService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public Mono<String> renderTemplateAsync(String templateName, Map<String, Object> model) {
        return Mono.fromCallable(() -> {
                    Context context = new Context();
                    context.setVariables(model);
                    return templateEngine.process(templateName, context);
                })
                // Execute rendering on a separate thread to prevent blocking the Netty event loop
                .subscribeOn(Schedulers.boundedElastic());
    }
}
