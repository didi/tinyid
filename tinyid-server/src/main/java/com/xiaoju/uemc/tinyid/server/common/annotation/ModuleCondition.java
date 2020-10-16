package com.xiaoju.uemc.tinyid.server.common.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

public class ModuleCondition implements Condition {
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Module.class.getName());
        if (attributes == null) {
            return true;
        }
        String prefix = "";
        Object prefixValue = attributes.getFirst("prefix");
        if (prefixValue != null) {
            prefix = prefixValue.toString();
        }
        final Environment environment = context.getEnvironment();
        for (Object value : attributes.get("value")) {
            String[] moduleName = (String[]) value;
            for (String module : moduleName) {
                String propertyName;
                if (prefix.equals("")) {
                    propertyName = module;
                } else {
                    propertyName = prefix + "." + module;
                }
                if (environment.getProperty(propertyName, boolean.class, false)) {
                    return true;
                }
            }
        }
        return false;
    }
}
