package demosecurity.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.util.Map;

@Component
public class ThymeleafRenderer {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public ThymeleafRenderer(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String renderThymeleafTemplate(String templateName, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);
        return templateEngine.process(templateName, context);
    }
}