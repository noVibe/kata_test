package service;

import model.Expression;

public class CalcService {
    public static void calculate(String s) {
        Expression e = ExpressionFabric.getExpression(s);
        System.out.println(e.execute());
    }



}
