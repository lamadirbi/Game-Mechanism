package com.lama;

import java.util.Stack;

public class MathExpressionEvaluator {

    public static double evaluate(String expression) throws IllegalArgumentException {
        if (expression == null || expression.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        expression = expression.replaceAll("\\s+", "");
        return parseExpression(expression);
    }

    private static double parseExpression(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                values.push(Double.parseDouble(sb.toString()));
            } else if (ch == '(') {
                ops.push(ch);
            } else if (ch == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                if (!ops.isEmpty() && ops.peek() == '(') {
                    ops.pop();
                }
            } else if (isOperator(ch)) {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(ch);
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + ch);
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return 0;
    }

    private static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }
        throw new UnsupportedOperationException("Unsupported operator: " + op);
    }
}
