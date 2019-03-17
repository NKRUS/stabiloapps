package ru.kit.stabiloapps.dynamicTest.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ru.kit.stabilo.StabiloController;
import ru.kit.stabiloapps.dynamicTest.DynamicTestStage;
import ru.kit.stabiloapps.dynamicTest.model.HomeCircle;
import ru.kit.stabiloapps.dynamicTest.model.MyCircle;
import ru.kit.stabiloapps.dynamicTest.util.Util;

import static ru.kit.stabiloapps.dynamicTest.model.Constants.*;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class DynamicTestController {

    public Group group;
    public AnchorPane startPane;
    public volatile Label counterLabel;
    public Label time;
    public AnchorPane endPane;
    public Button buttonOk;
    public ProgressBar progressBar;
    public Button buttonTest;
    private Rectangle borderGroup;
    private DynamicTestStage stage;
    private StabiloController stabilo;
    private List<MyCircle> figureList = new ArrayList<>();
    private static int WIDTH = 950;
    private static int HEIGHT = 950;
    private List<Task> tasks = new LinkedList<>();
    private MyCircle cursor;
    private Point correctionToCursor = new Point(0, 0);
    private boolean onStabilo = false;

    public void setStage(DynamicTestStage stage) {
        this.stage = stage;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @FXML
    public void initialize() {
        stabilo = new StabiloController();
        stabilo.onDynamicData();
        addFiguresInGroup();

        tasks.add(controlCursor);
        Thread t = new Thread(controlCursor);
        t.setDaemon(true);
        t.start();
    }

    private Task<Void> controlCursor = new Task<Void>() {
        @Override
        protected Void call() {

            while (!this.isCancelled()) {
                draw(stabilo.getX() - correctionToCursor.getX(), stabilo.getY() - correctionToCursor.getY());
                figureList.forEach(s->{
                    if (cursor.isIntersect(s)) {s.setFill(Color.RED);}
                    else {s.setFill(s.getDefaultColor());}
                });
//                Thread.sleep(0);
            }
            return null;
        }
    };

//    private Task<Void> monitorsScreenSize = new Task<Void>() {
//        @Override
//        protected Void call() throws Exception {
//            System.out.println("Width1");
//
//            return null;
//        }
//    };

    private void draw(double x, double y) {
        double centerX = WIDTH / 2;
        double centerY = HEIGHT / 2;
        double multiplier = WIDTH / 150;

        if (onStabilo) {
            cursor.draw(centerX + multiplier * x, centerY - multiplier * y, WIDTH, HEIGHT);
        } else {
            cursor.draw(WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT);
        }

//        LOG.info("({}, {})", centerX + multiplier * x, centerY - multiplier * y);

    }

    private void initializeFigureList() {
        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;
        double centerPointSize = centerX * 0.25;
        figureList.add(new HomeCircle(centerX, centerY, centerPointSize, Color.ORANGE));
        double pointSize = centerX * 0.16;
        figureList.add(new MyCircle(centerX * 0.45, centerY * 0.45, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX * 1.55, centerY * 1.55, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX * 0.45, centerY * 1.55, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX * 1.55, centerY * 0.45, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX * 0.23, centerY, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX * 1.77, centerY, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX, centerY * 0.23, pointSize, Color.AQUA, false));
        figureList.add(new MyCircle(centerX, centerY * 1.77, pointSize, Color.AQUA, false));
    }

    private Timeline timer;
    private Task<Void> startTimer;
    private volatile int[] totalMiliSeconds = {0};

    public void onStart() {
        stopAndStartTimer();
        rePain();
        startTimer = new Task<Void>() {
            @Override
            protected Void call() {

                threeTwoOne();
                onStabilo = true;
                progressBar.setProgress(0);

                timer = new Timeline(new KeyFrame(Duration.millis(UPDATE_TIME),
//                        ae -> time.setText(String.valueOf(totalMiliSeconds[0] += UPDATE_TIME))));
                        ae -> {progressBar.setProgress((totalMiliSeconds[0] += UPDATE_TIME) / END_TIME);


                        }));
                timer.setCycleCount(Timeline.INDEFINITE);
                Thread thread = new Thread(getStartGameTask());
                thread.setDaemon(true);
                thread.start();
                timer.play();


                while (!isCancelled()) {

                    if (totalMiliSeconds[0] >= END_TIME) {
//                        getStartGameTask().cancel();
                        timer.stop();
                        endPane.setVisible(true);
                        buttonOk.setDisable(false);
                        this.cancel();
                    }
                }

                totalMiliSeconds[0] = 0;
                onStabilo = false;
                timer.stop();
                return null;
            }
        };

        Thread t = new Thread(startTimer);
        t.setDaemon(true);
        t.start();
    }

    private void threeTwoOne() {
        endPane.setVisible(false);
        startPane.setVisible(true);

        for (int i = 3; i > 0; i--) {
            final int counter = i;
            Platform.runLater(() -> counterLabel.setText(String.valueOf(counter)));
            sleep(1000);
        }

        startPane.setVisible(false);
    }

    private void stopAndStartTimer() {
        if (startTimer != null && startTimer.isRunning()) {
            startTimer.cancel();
        }
        if (timer != null) {
            timer.stop();
        }
    }

    private boolean itWasFigureIsHold(MyCircle figure) {
        if (figure == null) return false;

        if (cursor.isIntersect(figure)) {
            figure.setPassFigure(true);
            return true;
        }
        return false;

//        if (cursor.isIntersect(figure) && figure.getHoldTime() <= HOLD_TIME) {
//            figure.setHoldTime(figure.getHoldTime() + UPDATE_TIME);
//            return false;
//        }
//        else if (!cursor.isIntersect(figure)){
//            figure.setHoldTime(0);
//            return false;
//        }
//        else {
//            figure.setPassFigure(true);
//            return true;
//        }
    }


    public void onReset() {
        correctionToCursor.setLocation(stabilo.getX(), stabilo.getY());
    }

    public void onCancel() {
        stage.close();
    }

    public void onSave() {
        Map<String, String> map = new HashMap<>();
        map.put("dynamic_test", String.valueOf(figureList.stream().filter(MyCircle::isPassFigure).collect(Collectors.toList()).size()));
        Util.writeJSON(stage.getPath(), map);
        stage.close();
    }

    public void addFiguresInGroup() {

        if (figureList != null) {
            figureList.clear();
            group.getChildren().clear();
            initializeFigureList();
        }
        assert figureList != null;
        figureList.forEach(s-> group.getChildren().add(s));

        borderGroup = new Rectangle(WIDTH, HEIGHT);
        borderGroup.setFill(Color.TRANSPARENT);
        group.getChildren().add(borderGroup);

        cursor = new MyCircle(500, 500, WIDTH * 0.032);
        cursor.setFill(Color.YELLOW);
        group.getChildren().add(cursor);
    }

    private void rePain() {
        figureList.forEach(s-> s.setVisible(false));
        figureList.get(0).setVisible(true);
    }

    private MyCircle figure = null;
    private Task<Void> startGame;
    private Task<Void> getStartGameTask() {
        if (startGame != null) {
            startGame.cancel();
        }
        return startGame = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                while (totalMiliSeconds[0] < TIMEOUT_BETWEEN_FIGURES) {
                    sleep(20);
                }
                for (int i = 1; i < figureList.size() && !isCancelled(); i++) {
                    System.out.println(Thread.currentThread().getName());
                    figure = figureList.get(i);
                    figure.setVisible(true);

                    while (totalMiliSeconds[0] < START_TIMER && !isCancelled()) {
                        sleep(20);
                    }
                    while (!itWasFigureIsHold(figure) && !isCancelled()) {
                        sleep(20);
                    }

                    while (!itWasFigureIsHold(figureList.get(0)) && !isCancelled()) {
                        sleep(20);
                    }
                }

                return null;
            }
        };
    }

    private void sleep(int miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onStartTest(ActionEvent event) {
        if (buttonTest.getText().contains("ТЕСТ")) {
            onStabilo = true;
            buttonTest.setText("СТОП");
        } else {
            onStabilo = false;
            buttonTest.setText("ТЕСТ");
        }

    }

    public StabiloController getStabilo() {
        return stabilo;
    }
}
