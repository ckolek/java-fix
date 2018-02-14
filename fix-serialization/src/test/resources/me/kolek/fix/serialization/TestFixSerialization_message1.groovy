message(name: 'TestMessage', msgType: 'XYZ') {
    component(name: 'TestComponent') {
        field(name: 'TestField1', tagNum: 10001, serDes: new StringSerDes())
        field(name: 'TestField2', tagNum: 10002, serDes: new IntSerDes(null))
        field(name: 'TestField3', tagNum: 10003, serDes: new BooleanSerDes())
    }
    group(name: 'TestGroup', numInGroupTagNum: 10101) {
        field(name: 'TestField4', tagNum: 10004, serDes: new CharSerDes())
        field(name: 'TestField5', tagNum: 10005, serDes: new MultipleCharValueSerDes())
        field(name: 'TestField6', tagNum: 10006, serDes: new MultipleStringValueSerDes())
    }
    field(name: 'TestField7', tagNum: 10007, serDes: new DataSerDes())
    field(name: 'TestField8', tagNum: 10008, serDes: new LocalMktDateSerDes('yyyyMMdd'))
    field(name: 'TestField9', tagNum: 10009, serDes: new FloatSerDes(10.0, '0.####'))
}