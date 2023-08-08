package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Graphics extends JPanel implements ActionListener {

    final static int WIDTH = 1000;
    final static int HEIGHT = 1000;
    final static int TICK_SIZE = 50;
    final static int BOARD_SIZE = (WIDTH * WIDTH) / (TICK_SIZE * TICK_SIZE);

    final Font font = new Font("TimesRoman",Font.BOLD,30);
    int[] snakePosX = new int[BOARD_SIZE];
    int[] snakePosY = new int[BOARD_SIZE];
    int snakeLength;
    Food food;
    int foodEaten;
    char direction = 'R';
    boolean isMoving = false;
    final Timer timer = new Timer(150,this);


    public Graphics(){
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.white);
        this.setFocusable(true);
        start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isMoving){
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_LEFT:
                            if (direction != 'R')
                                direction = 'L';
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (direction != 'L')
                                direction = 'R';
                            break;
                        case KeyEvent.VK_UP:
                            if (direction != 'D')
                                direction = 'U';
                            break;
                        case KeyEvent.VK_DOWN:
                            if (direction != 'U')
                                direction = 'D';
                            break;
                    }
                } else {
                    start();
                }
            }
        });
    }

    protected void start(){
        snakePosX = new int[BOARD_SIZE];
        snakePosY = new int[BOARD_SIZE];
        snakeLength = 3;
        foodEaten = 0;
        direction = 'R';
        isMoving = true;
        spawnFood();
        timer.start();
    }

    protected void move(){
        for (int i = snakeLength; i > 0; i--) {
            snakePosX[i] = snakePosX[i-1];
            snakePosY[i] = snakePosY[i-1];
        }
        switch (direction){
            case 'U' -> snakePosY[0] -= TICK_SIZE;
            case 'D' -> snakePosY[0] += TICK_SIZE;
            case 'L' -> snakePosX[0] -= TICK_SIZE;
            case 'R' -> snakePosX[0] += TICK_SIZE;
        }
    }

    protected void spawnFood(){
        food = new Food();
    }

    protected void eatFood(){
        if ((snakePosX[0] == food.getPosX()) && snakePosY[0] == food.getPosY()){
            snakeLength++;
            foodEaten ++;
            spawnFood();
        }
    }
    protected void collisionTest(){
        for (int i = snakeLength; i > 0 ; i--) {
            if ((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])){
                isMoving = false;
                break;
            }
        }
        if (snakePosX[0] < 0 || snakePosX[0] > WIDTH - TICK_SIZE || snakePosY[0] < 0 || snakePosY[0] > HEIGHT - TICK_SIZE){
            isMoving = false;
        }

        if (!isMoving){
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (isMoving){
            g.setColor(Color.BLUE);
            g.fillOval(food.getPosX(),food.getPosY(),TICK_SIZE,TICK_SIZE);
            g.setColor(Color.DARK_GRAY);
            for (int i = 0; i < snakeLength; i++) {
                g.fillRect(snakePosX[i],snakePosY[i],TICK_SIZE,TICK_SIZE);
            }
        } else {
            String scoreText = String.format("The end... Score: %d... Press any key to play again! ",foodEaten);
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(scoreText,(WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText))/2,HEIGHT/2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (isMoving){
            move();
            collisionTest();
            eatFood();
        }
        repaint();
    }
}
