package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static int RUNNING_SPEED = 50;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int pointsTaken;
    int pointX;
    int pointY;
    char direction = 'D';
    boolean running = true;
    boolean paused = false;
    Timer timer;
    Random random;
    JButton start = new JButton("Play");
    JButton exit = new JButton("Exit");

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapterHandler());
        startGame();
    }

    public void startGame() {
        running = true;
        timer = new Timer(RUNNING_SPEED, this);
        timer.start();
        nextPoint();
    }

    public void newTimer() {
        if(RUNNING_SPEED < 30) return;

        RUNNING_SPEED--;
        timer.stop();
        timer = new Timer(RUNNING_SPEED, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(paused){
            g.setColor(Color.white);
            g.setFont(new Font("Arial Black", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Paused", (SCREEN_WIDTH - metrics.stringWidth("Paused"))/2 , SCREEN_HEIGHT / 2);
        }
        if(running && !paused){
            g.setColor(Color.red);
            g.fillOval(pointX, pointY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++){
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE - 3, UNIT_SIZE - 3);
            }

            score(g);
        }
        if(!running && !paused){
            gameOver(g);
        }
    }

    public void nextPoint() {
        boolean isGoodCords = false;
        while(!isGoodCords){
            int tempX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            int tempY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

            for(int i = 0; i < bodyParts; i++){
                if(x[i] != tempX && y[i] != tempY){
                    pointX = tempX;
                    pointY = tempY;
                    isGoodCords = true;
                }
                else {
                    isGoodCords = false;
                }
            }
        }
        newTimer();
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'W':
                y[0] = y[0] - UNIT_SIZE;
                return;
            case 'S':
                y[0] = y[0] + UNIT_SIZE;
                return;
            case 'D':
                x[0] = x[0] + UNIT_SIZE;
                return;
            case 'A':
                x[0] = x[0] - UNIT_SIZE;
                return;
        }
    }

    public void checkPoint() {
        if((x[0] == pointX) && (y[0] == pointY)){
            bodyParts++;
            pointsTaken++;
            nextPoint();
        }
    }

    public void checkCollisions() {
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        if(x[0] < 0) {
            running = false;
            paused = false;
        }
        if(x[0] > SCREEN_HEIGHT-UNIT_SIZE){
            running = false;
            paused = false;
        }
        if(y[0] < 0){
            running = false;
            paused = false;
        }

        if(y[0] > SCREEN_HEIGHT-UNIT_SIZE){
            running = false;
            paused = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void score(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial Black", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score " + pointsTaken * 100, (SCREEN_WIDTH - metrics.stringWidth("Score " + pointsTaken * 100)) , g.getFont().getSize());
    }


    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Arial Black", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2 , SCREEN_HEIGHT / 2);


        score(g);
        RUNNING_SPEED = 60;

        bodyParts = 3;
        pointsTaken = 0;
        x[bodyParts] = 0;

        for(int i = 1; i < bodyParts; i++){
            x[i - 1] = UNIT_SIZE * (bodyParts - i);
            y[i - 1] = 0;
        }

        direction = 'D';
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running && !paused){
            move();
            checkPoint();
            checkCollisions();
        }
        repaint();
    }

    public class KeyAdapterHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event){
            switch(event.getKeyCode()){
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if(direction == 'D') return;
                    direction = 'A';
                    return;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if(direction == 'A') return;
                    direction = 'D';
                    return;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if(direction == 'S') return;
                    direction = 'W';
                    return;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if(direction == 'W') return;
                    direction = 'S';
                    return;
                case KeyEvent.VK_SPACE:
                    if(running) return;
                    startGame();
                    return;
                case KeyEvent.VK_ESCAPE:
                    if(!running) return;
                    paused = !paused;
            }
        }
    }
}
