package calculator3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


public class project extends JFrame {

    private HashMap <String, JButton> mathematical;
 
    private Stack <Double> stack;

    private JLabel screen;
 
    private static boolean ScreenReset;
 
    private double lastNum;
 
    private static boolean visibleOp;
   
    private BinaryOp lastOp;

        public project() {
        mathematical = new HashMap<String, JButton>();
        stack = new Stack<Double>();
        setLocation(20, 50);
        setTitle("Calculator");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initiliaze();
    }

    
    private void initiliaze() {
        CalDes pane = new CalDes(5);
        TextField z = new TextField(" Take Note Here: ");
       
        z.setForeground(Color.WHITE);
		z.setBackground(Color.BLACK);
		z.setEditable(true);
		add(z, BorderLayout.AFTER_LAST_LINE);
		
        JPanel mainpanel = new JPanel(pane);
        mainpanel.setBackground(Color.BLACK);

        screen = new JLabel("0", SwingConstants.LEFT);
        screen.setMinimumSize(new Dimension(75, 75));
        screen.setVerticalAlignment(SwingConstants.CENTER);
        screen.setBackground(Color.LIGHT_GRAY);
        screen.setOpaque(true);
       
        screen.setFont(new Font(screen.getFont().toString(), Font.BOLD, 30));

        mainpanel.add(screen, new SetPos(1, 1));

        addNumberButtons(mainpanel);
        addOperatorButtons(mainpanel);
        addFunctionButtons(mainpanel);
        addControlButtons(mainpanel);
        getContentPane().add(mainpanel);
    }

  
    private void addControlButtons(JPanel p) {
        
        JButton push = createButton("PUSH");
        JButton clearr = createButton("DEL");
        JButton back = createButton("RES");
        JButton pop = createButton("POP");
        JRadioButton visible = new JRadioButton("Show");

        
        
        push.setToolTipText("Takes number into memory");
        
        pop.setToolTipText("Returns numbers from memory");
        
        visible.setToolTipText("Show mathematical operations");

        
     
        
        ActionListener clear = e -> {
            screen.setText("0");
        };

        ActionListener reset = e -> {
            screen.setText("0");
            ScreenReset = true;
            lastNum = 0.0;
            visible.setSelected(false);
            lastOp = null;
        };

        ActionListener pushStack = e -> {
            String text = screen.getText();
            double number;
            try {
                number = Double.parseDouble(text);
                stack.push(number);
            } catch (NumberFormatException ign) {
            }
        };

        ActionListener popStack = e -> {
            if (stack.isEmpty()) {
                screen.setText("Empty stack, please push some value");
                ScreenReset = true;
                return;
            }
            Double number = stack.pop();
            screen.setText(number.toString());
        };

        visible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (visible.isSelected()) {
                    visibleOp = true;
                } else {
                    visibleOp = false;
                }
                inverseButtonName();
                repaint();
            }
        });

        clearr.addActionListener(clear);
        back.addActionListener(reset);
        push.addActionListener(pushStack);
        pop.addActionListener(popStack);

        p.add(clearr, "1,7");
        p.add(back, new SetPos(1, 8));
        p.add(push, new SetPos(2, 7));
        p.add(pop, new SetPos(2, 8));
        p.add(visible, new SetPos(5, 8));
    }

   
    private void addFunctionButtons(JPanel p) {
        ActionListener execOperation = e -> {
            JButton button = (JButton) e.getSource();
            String buttonName = button.getText();
            UnaryOp operation = getUnaryOperation(buttonName);

            double value = Double.parseDouble(screen.getText());
            double result = executeUnaryOperation(operation, value);
            screen.setText(Double.toString(result));
        };

        ActionListener xPownOp = e -> {
            executeBinaryOperation(new PownOperation());
        };

        JButton invx = createButton("1/x");
        invx.addActionListener(execOperation);

        JButton xpown = createButton("x^n");
        xpown.addActionListener(xPownOp);

        JButton sine = createButton("sin");
        sine.addActionListener(execOperation);

        JButton log = createButton("log");
        log.addActionListener(execOperation);

        JButton cosine = createButton("cos");
        cosine.addActionListener(execOperation);

        JButton ln = createButton("ln");
        ln.addActionListener(execOperation);

        JButton tan = createButton("tan");
        tan.addActionListener(execOperation);

        JButton ctg = createButton("ctg");
        ctg.addActionListener(execOperation);

        p.add(invx, new SetPos(2, 1));
        p.add(sine, new SetPos(2, 2));
        p.add(log, new SetPos(3 , 1));
        p.add(cosine, new SetPos(3, 2));
        p.add(ln, new SetPos(4, 1));
        p.add(tan, new SetPos(4, 2));
        p.add(xpown, new SetPos(5, 1));
        p.add(ctg, new SetPos(5, 2));
    }

  
    private UnaryOp getUnaryOperation(String buttonName) {
        switch (buttonName) {
        case "1/x":
            return new visibleOp();
        case "sin":
            return new SinOp();
        case "cos":
            return new CosOp();
        case "tan":
            return new TanOp();
        case "ctg":
            return new CtgOp();
        case "log":
            return new LogOp();
        case "ln":
            return new LnOp();
        case "asin":
            return new SinOp();
        case "acos":
            return new CosOp();
        case "atan":
            return new TanOp();
        case "actg":
            return new CtgOp();
        case "10^x":
            return new LogOp();
        case "e^x":
            return new LnOp();
        default:
            return null;
        }
    }

  
    private double executeUnaryOperation(UnaryOp operation, double value) {
        return operation.execute(value);
    }

    private void addNumberButtons(JPanel p) {
        JButton[] numbers = new JButton[10];

        ActionListener action = e -> {
            JButton button = (JButton) e.getSource();
            String number = button.getText();
            String text = screen.getText();
            if (text.equals("0")) {
                screen.setText("");
            }
            if (ScreenReset) {
                screen.setText("");
            }
            screen.setText(screen.getText() + number);
            ScreenReset = false;
        };

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = createButton(Integer.toString(i));
            numbers[i].setForeground(Color.red);
            numbers[i].setBackground(Color.WHITE);
            numbers[i].addActionListener(action);
        }
        p.add(numbers[0], new SetPos(5, 4));
        for (int i = 0; i < 3; i++) {
            p.add(numbers[i + 1], new SetPos(4, 3 + i));
        }
        for (int i = 3; i < 6; i++) {
            p.add(numbers[i + 1], new SetPos(3, i));
        }
        for (int i = 6; i < 9; i++) {
            p.add(numbers[i + 1], new SetPos(2, i - 3));
        }
    }

 
    private void addOperatorButtons(JPanel p) {
        ActionListener decimalPoint = e -> {
            if (!screen.getText().contains(".")) {
                screen.setText(screen.getText() + ".");
            }
        };

        ActionListener signChange = e -> {
            String text = screen.getText();
            if (text.equals("0")) {
                return;
            }
            if (text.startsWith("-")) {
                screen.setText(text.substring(1));
            } else {
                screen.setText("-" + text);
            }
        };

        ActionListener divOp = e -> {
            executeBinaryOperation(new DivisionOperation());
        };

        ActionListener mulOp = e -> {
            executeBinaryOperation(new MultiplyOperation());
        };

        ActionListener plusOp = e -> {
            executeBinaryOperation(new AdditionB());
        };

        ActionListener subOp = e -> {
            executeBinaryOperation(new SubOperation());
        };

        ActionListener result = e -> {
            executeBinaryOperation(null);
        };

        JButton equal = createButton("=");
        equal.addActionListener(result);

        JButton division = createButton("/");
        division.addActionListener(divOp);

        JButton multiply = createButton("*");
        multiply.addActionListener(mulOp);

        JButton minus = createButton("-");
        minus.addActionListener(subOp);

        JButton plus = createButton("+");
        plus.addActionListener(plusOp);

        JButton decimal = createButton(".");
        decimal.addActionListener(decimalPoint);

        JButton sign = createButton("+/-");
        sign.addActionListener(signChange);

        p.add(equal, new SetPos(1, 6));
        p.add(division, new SetPos(2, 6));
        p.add(multiply, new SetPos(3, 6));
        p.add(minus, new SetPos(4, 6));
        p.add(plus, new SetPos(5, 6));
        p.add(decimal, new SetPos(5, 5));
        p.add(sign, new SetPos(5, 3));
    }

   
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLUE);
      
        button.setMinimumSize(new Dimension(50, 40));
        button.setPreferredSize(new Dimension(75, 60));
        button.setFont(new Font(button.getFont().toString(), Font.BOLD, 15));
        mathematical.put(text, button);
        return button;
    }

    /**
     * Depolanmýþ deðerler üzerinde verilen ikili iþlemi yürütür.
     
    
     */
    private void executeBinaryOperation(BinaryOp operator) {
        Double value;
        try {
            value = Double.parseDouble(screen.getText());
        } catch (NumberFormatException e) {
            screen.setText("Invalid operation");
            ScreenReset = true;
            return;
        }
        if (lastOp != null) {
            double result = processLastOperator();
            screen.setText(Double.toString(result));
            lastNum = result;
        } else {
            lastNum = value;
        }
        ScreenReset = true;
        lastOp = operator;
    }

   
    double processLastOperator() {
        double result = 0;
        if (lastOp != null) {
            double valueInDisp = Double.parseDouble(screen.getText());
            result = lastOp.execute(lastNum, valueInDisp);
        }
        return result;
    }

   
    private interface BinaryOp {
      
        public double execute(double value1, double value2);
    }

    
    
  
    private static class AdditionB implements BinaryOp {
       
        public double execute(double value1, double value2) {
            return value1 + value2;
        }
    }

   
    private static class SubOperation implements BinaryOp {
  
        public double execute(double value1, double value2) {
            return value1 - value2;
        }
    }

 
    private static class MultiplyOperation implements BinaryOp {
        @Override
        public double execute(double value1, double value2) {
            return value1 * value2;
        }
    }

    
    private static class DivisionOperation implements BinaryOp {
     
        public double execute(double value1, double value2) {
            return value1 / value2;
        }
    }

    /**
     *      * 
     * @author ILYAS KECECI
     * @version 1.0
     *
     */
    private static class PownOperation implements BinaryOp {
        @Override
        public double execute(double value1, double value2) {
            if (visibleOp) {
                return Math.pow(value2, 1.0 / value1);
            }
            return Math.pow(value1, value2);
        }
    }

   
    private interface UnaryOp {
       
        public double execute(double value);
    }

  
    private static class SinOp implements UnaryOp {
     
        public double execute(double value) {
            ScreenReset = true;
            if (visibleOp) {
                return Math.asin(value);
            }
            return Math.sin(value);
        }
    }

  
    private static class CosOp implements UnaryOp {
      
        public double execute(double value) {
            ScreenReset = true;
            if (visibleOp) {
                return Math.acos(value);
            }
            return Math.cos(value);
        }
    }

   
    private static class TanOp implements UnaryOp {
       
        public double execute(double value) {
            ScreenReset = true;
            if (visibleOp) {
                return Math.atan(value);
            }
            return Math.tan(value);
        }
    }

   
    private static class CtgOp implements UnaryOp {
      
        public double execute(double value) {
            ScreenReset = true;
            if (visibleOp) {
                return Math.PI / 2 - Math.atan(value);
            }
            return 1 / Math.tan(value);
        }
    }

   
    private static class LogOp implements UnaryOp {
        
        public double execute(double value) {
            ScreenReset = true;
            if (visibleOp) {
                return Math.pow(10, value);
            }
            return Math.log10(value);
        }
    }

    
    private static class LnOp implements UnaryOp {
    
        public double execute(double value) {
            ScreenReset = true;
            if (visibleOp) {
                return Math.pow(Math.E, value);
            }
            return Math.log(value);
        }
    }

   
    private static class visibleOp implements UnaryOp {
     
        public double execute(double value) {
            ScreenReset = true;
            return 1.0 / value;
        }
    }

   
    private void inverseButtonName() {
        if (visibleOp) {
            mathematical.get("sin").setText("sinus");
            mathematical.get("tan").setText("tang");
            mathematical.get("ctg").setText("arctg");
            mathematical.get("cos").setText("cosinus");
            mathematical.get("ln").setText("e^x");
            mathematical.get("log").setText("10^x");
            mathematical.get("x^n").setText("n\u221Ax");
        } else {
            mathematical.get("sin").setText("sin");
            mathematical.get("tan").setText("tan");
            mathematical.get("ctg").setText("ctg");
            mathematical.get("cos").setText("cos");
            mathematical.get("ln").setText("ln");
            mathematical.get("log").setText("log");
            mathematical.get("x^n").setText("x^n");
        }
    }

    /**
     * Start point of program Calculator.
     * 
     * @author ILYAS KECECI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
            } catch (Exception e) {
            }
            JFrame frame = new project();
            Dimension ortala = Toolkit.getDefaultToolkit().getScreenSize();
            frame.pack();
           
            
            frame.setTitle("Advanced Calculator by Ilyas Kececi");
            frame.setMinimumSize(new Dimension(450, 350));
           
            frame.setLocation(ortala.width/2-frame.getSize().width/2, ortala.height/2-frame.getSize().height/2);
            frame.setVisible(true);
        });
    }

}