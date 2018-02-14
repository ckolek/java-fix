package me.kolek.fix.serialization

import me.kolek.fix.serialization.field.FieldSerDes
import me.kolek.fix.serialization.field.IntSerDes
import me.kolek.fix.serialization.metadata.*

abstract class SubstructureMetadataBuilder<M extends StructureMetadata> extends Script {
    private static long nextId = System.currentTimeMillis()

    protected final long id
    protected final String name
    protected final List<StructureMember> members

    SubstructureMetadataBuilder(Map<String, Object> params) {
        this.id = (long) params.getOrDefault('id', ++nextId)
        this.name = params.get('name')
        this.members = new ArrayList<>()
    }

    ComponentMetadata component(Map<String, Object> params, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder) Closure c) {
        member(new SubstructureMetadataBuilder<ComponentMetadata>(params) {
            @Override
            ComponentMetadata build() {
                new ComponentMetadata(id, name, members)
            }
        }, ComponentMember, params, c)
    }

    GroupMetadata group(Map<String, Object> params, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = SubstructureMetadataBuilder) Closure c) {
        member(new SubstructureMetadataBuilder<GroupMetadata>(params) {
            @Override
            GroupMetadata build() {
                int tagNum = (int) params.get('numInGroupTagNum')
                new GroupMetadata(id, name, new FieldMetadata<>(tagNum, tagNum, "No${name}s", new IntSerDes(null)), members)
            }
        }, GroupMember, params, c)
    }

    FieldMetadata field(Map<String, Object> params) {
        int tagNum = (int) params.get('tagNum')
        String name = params.get('name')
        FieldSerDes serDes = (FieldSerDes) params.get('serDes')
        member(FieldMember, new FieldMetadata(tagNum, tagNum, name, serDes), params)
    }

    protected <_M extends StructureMetadata> _M build(SubstructureMetadataBuilder<_M> builder, Closure c) {
        def code = c.rehydrate(builder, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code.run()

        return builder.build()
    }

    private <_M extends StructureMetadata> _M member(SubstructureMetadataBuilder<_M> builder, Class<? extends StructureMember> memberType, Map<String, Object> params, Closure c) {
        member(memberType, build(builder, c), params)
    }

    private <_M> _M member(Class<? extends StructureMember> memberType, _M metadata, Map<String, Object> params) {
        boolean trailing = params.getOrDefault('trailing', false)
        boolean required = params.getOrDefault('required', false)
        members.add(memberType.newInstance(metadata, members.size(), trailing, required))
        return metadata
    }

    abstract M build()

    @Override
    Object run() {
        return build()
    }
}
