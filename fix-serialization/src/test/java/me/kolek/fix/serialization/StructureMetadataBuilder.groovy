package me.kolek.fix.serialization

import me.kolek.fix.serialization.metadata.ComponentMetadata
import me.kolek.fix.serialization.metadata.GroupMetadata
import me.kolek.fix.serialization.metadata.MessageMetadata
import me.kolek.fix.serialization.metadata.StructureMetadata
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class StructureMetadataBuilder extends SubstructureMetadataBuilder<StructureMetadata> {
    private StructureMetadata root

    StructureMetadataBuilder() {
        super([:])
    }

    MessageMetadata message(Map<String, Object> params, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder) Closure c) {
        return setRoot(build(new SubstructureMetadataBuilder<MessageMetadata>(params) {
            @Override
            MessageMetadata build() {
                String msgType = params.get('msgType')
                return new MessageMetadata(id, name, msgType, members)
            }
        }, c))
    }

    @Override
    ComponentMetadata component(Map<String, Object> params, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder) Closure c) {
        setRoot(super.component(params, c))
    }

    @Override
    GroupMetadata group(Map<String, Object> params, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder) Closure c) {
        setRoot(super.group(params, c))
    }

    private <T extends StructureMetadata> T setRoot(T root) {
        if (this.root != null) {
            throw new IllegalStateException("root object has already been defined")
        }
        this.root = root
        return root
    }

    @Override
    StructureMetadata build() {
        return root
    }

    static <M extends StructureMetadata> M getMetadataResource(Class<?> contextClass, String resourceName) {
        def resource = contextClass.getResource("${resourceName}.groovy")

        def importCustomizer = new ImportCustomizer()
        importCustomizer.addStarImports('me.kolek.fix.serialization.field')

        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.setScriptBaseClass(StructureMetadataBuilder.name)
        compilerConfiguration.addCompilationCustomizers(importCustomizer)

        def shell = new GroovyShell(contextClass.getClassLoader(), compilerConfiguration)

        def script = shell.parse(resource.toURI())
        return (M) script.run()
    }
}
