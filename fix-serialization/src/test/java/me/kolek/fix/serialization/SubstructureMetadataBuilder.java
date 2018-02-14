package me.kolek.fix.serialization;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import groovy.lang.Script;
import me.kolek.fix.serialization.field.FieldSerDes;
import me.kolek.fix.serialization.field.IntSerDes;
import me.kolek.fix.serialization.metadata.*;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SubstructureMetadataBuilder<M extends StructureMetadata> extends Script {
    private static long nextId = System.currentTimeMillis();

    final long id;
    final String name;
    final List<StructureMember> members;

    SubstructureMetadataBuilder(Map<String, Object> params) {
        this.id = (long) params.getOrDefault("id", ++nextId);
        this.name = (String) params.get("name");
        this.members = new ArrayList<StructureMember>();
    }

    public ComponentMetadata component(final Map<String, Object> params,
            @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder.class) Closure c) {
        return member(new SubstructureMetadataBuilder<ComponentMetadata>(params) {
            @Override
            public ComponentMetadata build() {
                return new ComponentMetadata(id, name, members);
            }
        }, ComponentMember.class, params, c);
    }

    public GroupMetadata group(final Map<String, Object> params,
            @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder.class) Closure c) {
        return member(new SubstructureMetadataBuilder<GroupMetadata>(params) {
            @Override
            public GroupMetadata build() {
                int tagNum = (int) params.get("numInGroupTagNum");
                return new GroupMetadata(id, name,
                        new FieldMetadata(tagNum, tagNum, "No" + name + "s", new IntSerDes(null)), members);
            }
        }, GroupMember.class, params, c);
    }

    public FieldMetadata field(Map<String, Object> params) {
        int tagNum = (int) params.get("tagNum");
        String name = (String) params.get("name");
        FieldSerDes serDes = (FieldSerDes) params.get("serDes");
        return member(FieldMember.class, new FieldMetadata(tagNum, tagNum, name, serDes), params);
    }

    protected <_M extends StructureMetadata> _M build(SubstructureMetadataBuilder<_M> builder, Closure c) {
        Closure code = c.rehydrate(builder, this, this);
        code.setResolveStrategy(Closure.DELEGATE_ONLY);
        code.run();

        return builder.build();
    }

    private <_M extends StructureMetadata> _M member(SubstructureMetadataBuilder<_M> builder,
            Class<? extends StructureMember> memberType, Map<String, Object> params, Closure c) {
        return member(memberType, build(builder, c), params);
    }

    private <_M> _M member(Class<? extends StructureMember> memberType, _M metadata, Map<String, Object> params) {
        boolean trailing = (boolean) params.getOrDefault("trailing", false);
        boolean required = (boolean) params.getOrDefault("required", false);
        members.add(DefaultGroovyMethods
                .newInstance(memberType, new Object[]{metadata, members.size(), trailing, required}));
        return metadata;
    }

    public abstract M build();

    @Override
    public Object run() {
        return build();
    }
}
