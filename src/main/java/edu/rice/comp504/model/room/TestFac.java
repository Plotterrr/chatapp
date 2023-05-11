package edu.rice.comp504.model.room;

public class TestFac {

    public TestAbs make() {
        return new TestClass(1);
    }
}
