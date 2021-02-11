package com.github.ahenteti.dummyserver;

public class NoValueExpectation extends ValueExpectation {

    @Override
    public boolean isIncorrect(String value) {
        return false;
    }

    @Override
    public boolean isCorrect(String value) {
        return true;
    }
}
