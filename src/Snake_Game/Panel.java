import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JPanel;
public class Panel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=75;
    final int[] x =new int[GAME_UNITS];
    final int[] y =new int[GAME_UNITS];
    int bodyParts=6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction='R';
    boolean running=true;
    Timer timer;
    Random random;


    Panel(){

        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running= true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (running){
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            for(int i=0;i<bodyParts;i++){
                if(i==0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        else
            gameOver(g);
        //this is the score
        g.setColor(Color.red);
        g.setFont(new Font("Times new Romans",Font.BOLD,25));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Score =",(SCREEN_WIDTH-metrics1.stringWidth("Score ="+ applesEaten))/2,g.getFont().getSize());
    }
    public void newApple(){
        appleX=random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY=random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;


    }
    public void move(){
        for( int i=bodyParts;i>0;i--){
            //arrays are used here
            x[i]=x[i-1];
            y[i]=y[i-1];

        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }
    public void Apple(){
        if(x[0]==appleX && y[0]==appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }
    public void checkCollisions(){
        //this checks head collision with body
        try {
            for (int i =bodyParts;i>0;i--){
                if ((x[0] == x[i] && (y[0] == y[i]))) {
                    running = false;
                    break;
                }
            }
                //checks collision with left border
            if (x[0]<0)
                running=false;
                //checks if head touches right border
            if (x[0]>SCREEN_WIDTH)
                running=false;
            // if head touches bottom border
            if (y[0]> SCREEN_HEIGHT)
                running=false;
                //checks if head touches top border
            if (y[0] < 0)
                running=false;
            if (!running)
                timer.stop();
                }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Array is out bounds" );
        }
        if (!running)
            timer.stop();

    }
    public void gameOver(Graphics g){
        //this is score
        g.setColor(Color.red);
        g.setFont(new Font("Times New Romans",Font.BOLD,25));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Score =",(SCREEN_WIDTH-metrics1.stringWidth("Score ="+ applesEaten))/2,g.getFont().getSize());
        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("ink Free",Font.BOLD,75));
        FontMetrics metrics2=getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

    }
    @Override
    public void repaint(Rectangle r) {
        super.repaint(r);
    }
    public void actionPerformed(ActionEvent e){
        if (running) {
            move();
            Apple();
            checkCollisions();
            }
        repaint();
    }





    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            try {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != 'R')
                            direction = 'L';
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != 'L')
                            direction = 'R';
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != 'D')
                            direction = 'U';
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != 'U')
                            direction = 'D';
                    }
                }
            }
            catch (Exception ex){
                System.out.println(" Exception");
            }

        }

    }


}
