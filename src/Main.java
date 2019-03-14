import calculation.ExpressionCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Main extends JFrame {
    private int WIDTH = 350;
    private int HEIGTH = 420;
    private boolean IS_RESIZABLE = false;
    private String FONT_TYPE = "SansSerif";
    private int FONT_SIZE = 20;

    private String RESULT_LABEL = "Результат:";
    private String OPERATIONS_BUTTONS = "()789456123.0/*-+";
    private String BACKSPASE = "<-";
    private String RESULT = "=";
    private String[] BUTTONS = {
            "(", ")", "<-", "C",
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            ".", "0", "=", "+"};

    private JTextField expressionDisplay = new JTextField();
    private JTextField resultDisplay = new JTextField();

    private boolean isCalculated = false;


    Main() {
        super("Калькулятор");
        Box content = javax.swing.Box.createVerticalBox();
        JPanel panel = setDisplays(new JPanel(), expressionDisplay, resultDisplay);
        JPanel buttonsPanel = setButtons(new JPanel());

        content.add(panel);
        content.add(buttonsPanel);
        setContentPane(content);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGTH);
        setResizable(IS_RESIZABLE);
        setVisible(true);
    }

    private JPanel setDisplays(JPanel panel, JTextField expressionDisplay, JTextField resultDisplay) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel res = new JLabel(RESULT_LABEL);
        panel.add(expressionDisplay);
        panel.add(res);
        panel.add(resultDisplay);
        Font font1 = new Font(FONT_TYPE, Font.PLAIN, FONT_SIZE);
        expressionDisplay.setFont(font1);
        resultDisplay.setFont(font1);
        resultDisplay.setEditable(false);
        resultDisplay.setHorizontalAlignment(JTextField.RIGHT);
        expressionDisplay.setHorizontalAlignment(JTextField.RIGHT);
        res.setAlignmentX(Component.RIGHT_ALIGNMENT);
        return panel;
    }

    private JPanel setButtons(JPanel panel) {
        panel.setLayout(new GridLayout(5, 4));

        for (String buttonLabel : BUTTONS) {
            JButton button = new JButton(buttonLabel);
            button.addActionListener(this::onClick);
            panel.add(button);
        }
        return panel;
    }

    private void onClick(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            String s = ((JButton) e.getSource()).getText();
            if (OPERATIONS_BUTTONS.contains(s)) {
                if (isCalculated) {
                    isCalculated = false;
                    expressionDisplay.selectAll();
                    resultDisplay.setText("");
                }
                expressionDisplay.replaceSelection(s);
            } else if (s.equals(BACKSPASE)) {
                int len = expressionDisplay.getText().length();
                expressionDisplay.select(len - 1, len);
                expressionDisplay.replaceSelection("");
            } else if (s.equals("C")) {
                isCalculated = false;
                resultDisplay.setText("");
                expressionDisplay.setText("");
            } else if (s.equals(RESULT)) {
                isCalculated = true;
                resultDisplay.setText("");
                resultDisplay.setText(calculateExp(expressionDisplay.getText()));
            }
        }
    }

    private String calculateExp(String expression) {
        try {
            return String.valueOf(ExpressionCalculator.calculate(expression));
        } catch (IOException e) {
            return e.getMessage();
        } catch (ArithmeticException e) {
            return ExpressionCalculator.ZERO_DIVISION;
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
