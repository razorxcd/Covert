package vit.mtech.test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import javax.swing.text.DefaultCaret;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;




public class Test
{
    private static String ENTER = "ENTER";
    static JButton enterButton;
    static JButton button;
    public static JTextArea output;
    public static JTextArea inputTextArea;
    static JFrame frame;
    static JPanel panel;
    public static String testString = "test";
    public static KeyStroke keyStroke;
     
    private static final String INSERT_BREAK = "insert-break";
    private static final String TEXT_SUBMIT = "text-submit";

    public static void main(String... args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        createFrame();
    }
//     private static Action wrapper = new AbstractAction() {
//        @Override
//        public void actionPerformed(ActionEvent ae) {
//            
//            enterButton.doClick();
//        }
//    };

    public static void createFrame()
    {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        ButtonListener buttonListener = new ButtonListener();
        output = new JTextArea(50, 50);
        output.setWrapStyleWord(true);
        output.setEditable(false);
        JScrollPane scroller = new JScrollPane(output);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel inputpanel = new JPanel();
        inputpanel.setLayout(new BoxLayout(inputpanel, BoxLayout.Y_AXIS));
        inputTextArea = new JTextArea(" ",50,50);
        JScrollPane scrollr = new JScrollPane(inputTextArea);
        scrollr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        InputMap input = inputTextArea.getInputMap();
    KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
    KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
    input.put(shiftEnter, INSERT_BREAK);  // input.get(enter)) = "insert-break"
    input.put(enter, TEXT_SUBMIT);
    ActionMap actions = inputTextArea.getActionMap();
    actions.put(TEXT_SUBMIT, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            submitText();
        }

            private void submitText() {
                System.out.println("Working correctly");
            }
    });
       
        
      
        enterButton = new JButton("Enter");
        //enterButton.setActionCommand(ENTER);
         enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
               String sp=inputTextArea.getText();
                //System.out.println(sp);
                QueryV3 q=new QueryV3(sp);
                q.connecti();
                q.init();
                q.map();
                q.executeSubQueries();
                MultiMap res=q.printResults();
                Set keys=res.keySet();
                for(Object k:keys)
                {
                    output.append(k+"-->"+res.get(k));
                    output.append("\n");
                }
                q.close();
                //output.append(str);
                
                
                
            }
        });     
        // enterButton.setEnabled(false);
        //input.setActionCommand(ENTER);
        inputTextArea.addKeyListener((KeyListener) buttonListener);
        DefaultCaret caret = (DefaultCaret) output.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        panel.add(scroller);
        panel.add(scrollr);
        inputpanel.add(inputTextArea);
        inputpanel.add(enterButton);
        panel.add(inputpanel);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        // Center of screen
        // frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
        inputTextArea.requestFocus();
    }

    public static class ButtonListener implements KeyListener
    {

                public void actionPerformed(final ActionEvent ev)
        {
            
        }

        @Override
        public void keyTyped(KeyEvent e) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
        
            
            
            if (e.getKeyCode() == e.VK_ENTER) {

        //System.out.println(input.getText());

    }

//            if (!input.getText().trim().equals(""))
//            {
//                String cmd = e.getActi;
//                if (ENTER.equals(cmd))
//                {
//                    output.append(input.getText());
//                    if (input.getText().trim().equals(testString)) output.append(" = " + testString);
//                    else output.append(" != " + testString);
//                    output.append("\n");
//                }
//            }
//            input.setText("");
//            input.requestFocus();
//        }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}