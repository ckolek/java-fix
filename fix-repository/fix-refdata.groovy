import me.kolek.fix.repository.Repository

def repo = Repository.parse(new File(args[0]))

def printDataType
printDataType = { dataType ->
    if (dataType.baseType != null) {
        println "INSERT INTO FIX_FIELD_TYPE (TYPE_CD, PARENT_TYPE_CD) VALUES ('${dataType.name.toUpperCase()}', '${dataType.baseType.toUpperCase()}');"
    } else {
        println "INSERT INTO FIX_FIELD_TYPE (TYPE_CD) VALUES ('${dataType.name.toUpperCase()}');"
    }
    repo.getDataTypes(dataType).each(printDataType)
}
repo.rootDataTypes.each(printDataType)
def getFieldType = { field ->
    def fieldType = field.type.toUpperCase()
    switch (fieldType) {
        case "MULTIPLEVALUESTRING":
            return "MULTIPLESTRINGVALUE"
        default:
            return fieldType
    }

}
repo.fields.each { field ->
    println "INSERT INTO FIX_FIELD (FIELD_ID, NAME, TAG_NUM, TYPE_CD) VALUES ($field.tagNum, '$field.name', $field.tagNum, '${getFieldType(field)}');"
}
repo.messages.each { message ->
    println "INSERT INTO FIX_STRUCTURE (STRUCTURE_ID, NAME, TYPE_CD, MSG_TYPE) VALUES ($message.id, '$message.name', 'M', '$message.msgType');"
}
repo.components.each { component ->
    if (component.type.contains('Repeating')) {
        def noField = repo.getContents(component).get(0)
        println "INSERT INTO FIX_STRUCTURE (STRUCTURE_ID, NAME, TYPE_CD, NO_FIELD_ID) VALUES ($component.id, '$component.name', 'G', $noField.tagNum);"
    } else {
        println "INSERT INTO FIX_STRUCTURE (STRUCTURE_ID, NAME, TYPE_CD) VALUES ($component.id, '$component.name', 'C');"
    }
}
def printContent = { content, index ->
    content.asComponent { cContent ->
        def component = repo.getComponent(cContent.name)
        println "INSERT INTO FIX_STRUCTURE_ELEMENT (STRUCTURE_ID, FIX_VERSION, ORDER_NO, SUB_STRUCTURE_ID, REQUIRED) VALUES ($cContent.ownerId, '$repo.version', $index, $component.id, ${cContent.required ? 1 : 0});"
    }
    content.asField { fContent ->
        println "INSERT INTO FIX_STRUCTURE_ELEMENT (STRUCTURE_ID, FIX_VERSION, ORDER_NO, FIELD_ID, REQUIRED) VALUES ($fContent.ownerId, '$repo.version', $index, $fContent.tagNum, ${fContent.required ? 1 : 0});"
    }
}
repo.messages.each { message ->
    repo.getContents(message).eachWithIndex(printContent)
}
repo.components.each { component ->
    if (component.type.contains('Repeating')) {
        repo.getContents(component).drop(1).eachWithIndex(printContent)
    } else {
        repo.getContents(component).eachWithIndex(printContent)
    }
}