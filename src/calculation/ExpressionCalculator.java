package calculation;


import java.io.IOException;
import java.util.*;

public class ExpressionCalculator {
    private static final String OPERATORS = "+-*/";
    private static final String DELIMITERS = "()" + OPERATORS;

    private static final String UNARY_MINUS = "un";
    private static final String ADD = "+";
    private static final String SUB = "-";
    private static final String DIV = "/";
    private static final String MUL = "*";

    private static final String ERROR = "ОШИБКА: ";
    private static final String INCORRECT_EXPRESSION = ERROR + "некорректное выражение";
    private static final String UNCOORDINATED_BRACKET = ERROR + "неверное количество скобок";
    public static final String ZERO_DIVISION = ERROR + "деление на 0";


    private static String prepareExpression(String expression) {
        return expression.replaceAll(" ", "").replaceAll(",", ".");
    }

    private static boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        return DELIMITERS.contains(token);
    }

    private static boolean isOperator(String token) {
        if (token.equals(UNARY_MINUS)) return true;
        return OPERATORS.contains(token);
    }


    private static int priority(String token) {
        switch (token) {
            case "(":
                return 1;
            case ADD:
            case SUB:
                return 2;
            case MUL:
            case DIV:
                return 3;
            default:
                return 4;
        }
    }


    private static List<String> parseStringExpression(String expression) throws IOException {
        expression = prepareExpression(expression);
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, DELIMITERS, true);
        String previousOperator = "", currentOperator = "";

        while (tokenizer.hasMoreTokens()) {
            currentOperator = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(currentOperator)) {
                throw new IOException(INCORRECT_EXPRESSION);

            } else if (isDelimiter(currentOperator)) {
                if (currentOperator.equals("(")) stack.push(currentOperator);
                else if (currentOperator.equals(")") && !stack.isEmpty()) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            throw new IOException(UNCOORDINATED_BRACKET);
                        }
                    }
                    stack.pop();
                } else {
                    if ((currentOperator.equals(SUB) && previousOperator.equals("")) ||
                            (isDelimiter(previousOperator) && !previousOperator.equals(")"))) {
                        currentOperator = UNARY_MINUS;
                    } else {
                        while (!stack.isEmpty() && (priority(currentOperator) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }
                    }
                    stack.push(currentOperator);
                }

            } else {
                postfix.add(currentOperator);
            }
            previousOperator = currentOperator;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) postfix.add(stack.pop());
            else throw new IOException(UNCOORDINATED_BRACKET);
        }
        return postfix;
    }

    private static Float calculateSimpleExp(Float a, Float b, String operator) throws IOException {
        switch (operator) {
            case ADD:
                return a + b;
            case SUB:
                return a - b;
            case MUL:
                return a * b;
            case DIV:
                if (b == 0) throw new ArithmeticException(ZERO_DIVISION);
                return a / b;
            default:
                throw new IOException(INCORRECT_EXPRESSION);
        }
    }

    public static Float calculate(String expression) throws IOException {
        List<String> postfix = parseStringExpression(expression);

        Deque<Float> stack = new ArrayDeque<>();
        for (String op : postfix) {
            if (OPERATORS.contains(op)) {
                Float b = stack.pop(), a = stack.pop();
                Float result = calculateSimpleExp(a, b, op);
                stack.push(result);
            } else if (op.equals(UNARY_MINUS)) stack.push(-stack.pop());
            else stack.push(Float.valueOf(op));
        }
        return stack.pop();
    }
}
