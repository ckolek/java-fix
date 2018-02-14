package me.kolek.fix.serialization;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import me.kolek.fix.serialization.metadata.ComponentMetadata;
import me.kolek.fix.serialization.metadata.GroupMetadata;
import me.kolek.fix.serialization.metadata.MessageMetadata;
import me.kolek.fix.serialization.metadata.StructureMetadata;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class StructureMetadataBuilder extends SubstructureMetadataBuilder<StructureMetadata> {
    private StructureMetadata root;

    public StructureMetadataBuilder() {
        super(Collections.emptyMap());
    }

    public MessageMetadata message(final Map<String, Object> params,
            @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder.class) Closure c) {
        return setRoot(build(new SubstructureMetadataBuilder<MessageMetadata>(params) {
            @Override
            public MessageMetadata build() {
                String msgType = (String) params.get("msgType");
                return new MessageMetadata(id, name, msgType, members);
            }
        }, c));
    }

    @Override
    public ComponentMetadata component(Map<String, Object> params,
            @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder.class) Closure c) {
        return setRoot(super.component(params, c));
    }

    @Override
    public GroupMetadata group(Map<String, Object> params,
            @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder.class) Closure c) {
        return setRoot(super.group(params, c));
    }

    private <T extends StructureMetadata> T setRoot(T root) {
        if (this.root != null) {
            throw new IllegalStateException("root object has already been defined");
        }

        this.root = root;
        return root;
    }

    @Override
    public StructureMetadata build() {
        return root;
    }

    public static <M extends StructureMetadata> M getMetadataResource(Class<?> contextClass,
            final String resourceName) throws IOException, URISyntaxException {
        URL resource = contextClass.getResource(resourceName + ".groovy");

        ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addStarImports("me.kolek.fix.serialization.field");

        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(StructureMetadataBuilder.class.getName());
        compilerConfiguration.addCompilationCustomizers(importCustomizer);

        GroovyShell shell = new GroovyShell(contextClass.getClassLoader(), compilerConfiguration);

        Script script = shell.parse(resource.toURI());
        return (M) script.run();
    }
}
