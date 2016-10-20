package com.mathapp;

/**
 * Created by ahmha02 on 15-10-12.
 */
public class MathSimple {

    private Integer result = 0;
    public enum Oper{
        add,subtract,divide,multiply,stall,error;
    }

    public Integer process (String operation, Integer value1, Integer value2) {

        switch (Oper.valueOf(operation)) {
            case add:
                result = value1 + value2;
                break;
            case subtract:
                result = value1 - value2;
                break;
            case divide:
                result = value1 / value2;
                break;
            case multiply:
                result = value1 * value2;
                break;
            case stall:
                break;
            case error:

        }
        return result;
    }
}
