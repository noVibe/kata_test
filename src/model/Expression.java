package model;

import enums.ArithmeticOperation;
import enums.NumberFormat;
import exception.UnexpectedResultException;
import service.RomeArabicConverter;

import static enums.NumberFormat.ROMAN;

public record Expression(int firstNum, int secondNum, ArithmeticOperation operation, NumberFormat format){
    public String execute() {
        return setResult(validate(switch (operation) {
            case PLUS -> firstNum + secondNum;
            case MINUS -> firstNum - secondNum;
            case DIVIDE -> (double) firstNum / secondNum;
            case MULTIPLY -> firstNum * secondNum;
        }));
    }
    private String setResult(double d) {
        return switch (format) {
            case ARABIC -> String.valueOf(d);
            case ROMAN -> RomeArabicConverter.convert((int) d);
        };
    }

    private double validate(double d) {
        if (d < 1 || Math.rint(d) != d && format.equals(ROMAN)) {
            throw new UnexpectedResultException("Roman number must be natural and greater than 0");
        } return d;
    }
}
