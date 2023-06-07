package service;

import exception.InvalidInputException;
import model.Expression;

public class CalcService {
    private int restriction;
    public void calculate(String s) {
        Expression e = ExpressionFabric.getExpression(s);
        if (e.firstNum() > restriction || e.secondNum() > restriction) {
            throw new InvalidInputException(String.format("Number must be in range [1 - %d].", restriction));
        }
        System.out.println(e.execute(true));
    }

    public CalcService setRestriction(int restriction) {
        if (restriction < 1) {
            throw new IllegalArgumentException("Restriction must be positive.");
        }
        this.restriction = restriction;
        return this;
    }
}
