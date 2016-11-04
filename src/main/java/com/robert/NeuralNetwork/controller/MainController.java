package com.robert.NeuralNetwork.controller;

import com.robert.NeuralNetwork.algorithm.Adaline;
import com.robert.NeuralNetwork.algorithm.Neuron;
import com.robert.NeuralNetwork.algorithm.Perceptron;
import com.robert.NeuralNetwork.utils.DataPreparer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    private RadioButton perceptronRadioButton;
    @FXML
    private RadioButton adelineRadioButton;
    @FXML
    private RadioButton bipolarRadioButton;
    @FXML
    private RadioButton unipolarRadioButton;
    @FXML
    private TextField startTextField;
    @FXML
    private TextField endTextField;
    @FXML
    private TextField biasTextField;
    @FXML
    private TextField learningRateTextField;
    @FXML
    private TextField testInputAmount;
    @FXML
    private Slider learningRateSlider;
    @FXML
    private Button randomTestDataButton;
    @FXML
    private Button randomWeightsButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button initButton;
    @FXML
    private Button startButton;
    @FXML
    private Button nextStepButton;
    @FXML
    private Pane chartPane;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn x1Column;
    @FXML
    private TableColumn x2Column;
    @FXML
    private TableColumn yColumn;

    private LineChart<Number,Number> chart;

    private DataPreparer dataPreparer;
    private XYChart.Series dataSeries;
    private XYChart.Series weightsSeries;

    private Neuron neuron;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRadioButtons();
        initSlider();
        initButtons();
        initChart();

        dataPreparer = new DataPreparer();
    }

    private void initRadioButtons() {
        final ToggleGroup group = new ToggleGroup();
        perceptronRadioButton.setToggleGroup(group);
        adelineRadioButton.setToggleGroup(group);
        perceptronRadioButton.setSelected(true);

        final ToggleGroup group2 = new ToggleGroup();
        bipolarRadioButton.setToggleGroup(group2);
        unipolarRadioButton.setToggleGroup(group2);
        unipolarRadioButton.setSelected(true);
    }

    private void initSlider() {
        learningRateSlider.valueProperty().addListener((ov, old_val, new_val) -> learningRateTextField.setText(String.format("%.2f", new_val)));
    }

    private void initButtons() {
        randomTestDataButton.setOnAction(event -> prepareTestData());
        randomWeightsButton.setOnAction(event -> randomWeigths());
        startButton.setOnAction(event -> runAlgorithm ());
        nextStepButton.setOnAction(event -> nextStep ());
        resetButton.setOnAction(event -> reset ());
        initButton.setOnAction(event -> init ());
        startButton.setDisable(true);
        nextStepButton.setDisable(true);
    }

    private void initChart() {
        final NumberAxis xAxis = new NumberAxis(-1.1, 1.1, 0.1);
        final NumberAxis yAxis = new NumberAxis(-1.1, 1.1, 0.1);
        chart = new LineChart<>(xAxis,yAxis);
        chart.setPrefSize(900, 800);
        chartPane.getChildren().add(chart);

        dataSeries = new ScatterChart.Series();
        dataSeries.setName("Test Data");
        chart.getData().add(0, dataSeries);
        weightsSeries = new LineChart.Series();
        weightsSeries.setName("Weights Line");
        chart.getData().add(1, weightsSeries);
    }

    private void runAlgorithm() {
        double[][] inputData = dataPreparer.getInput();
        int[] outputs = dataPreparer.getOutput();

        int iteration = 0;
        double error ;
        do{
            error = 0;
            iteration++;
            for (int i = 0; i < inputData.length; i++) {
                error += neuron.learn(inputData[i], outputs[i]);
            }
            error /=inputData.length;
//
//            if (iteration % 10000000 == 0){
//                addWeightsToChar(neuron.getWeights(), neuron.getBias());
//                System.out.println(iteration);
//            }

        }while(error > 0.010  && iteration < 10000);

        System.out.println("totalErrors = " + error);
        System.out.println("iter number = " + iteration);
        System.out.println(neuron.getWeights()[0] + "\t" + neuron.getWeights()[1]+ "\t" + neuron.getBias() );
        addToTableView(inputData, outputs);


        addWeightsToChar(neuron.getWeights(), neuron.getBias());
    }

    private void nextStep() {
        double[][] inputData = dataPreparer.getInput();
        int[] outputs = dataPreparer.getOutput();

        int error = 0;
        for (int i = 0; i <inputData.length; i++) {
            error += neuron.learn(inputData[i], outputs[i]);
        }
        addWeightsToChar(neuron.getWeights(), neuron.getBias());
    }

    double w1;
    double w2;

    private void reset() {
        startButton.setDisable(true);
        nextStepButton.setDisable(true);

        double[] www = {w1, w2};
        dataPreparer.setWeights(www);
        System.out.println(www[0] + "  " + www[1]);
        System.out.println(w1 + "  " + w2);
        double bias = Double.valueOf(biasTextField.getText().replace(',', '.'));
        addWeightsToChar(www, bias);
    }

    private void init() {
        startButton.setDisable(false);
        nextStepButton.setDisable(false);

        if (dataPreparer.getWeights() == null){
            double startValue = Double.valueOf(startTextField.getText().replace(',', '.'));
            double endValue = Double.valueOf(endTextField.getText().replace(',', '.'));
            dataPreparer.randomWeights(startValue, endValue);
        }
        if (dataPreparer.getOutput() == null){
            int amount = Integer.valueOf(testInputAmount.getText());
            dataPreparer.initData(amount);
        }
        double bias = Double.valueOf(biasTextField.getText().replace(',', '.'));
        double learningRate = Double.valueOf(learningRateTextField.getText().replace(',', '.'));

        w1 = dataPreparer.getWeights()[0];
        w2 = dataPreparer.getWeights()[1];
        System.out.println(w1 + "  " + w2);
        if (chooseTrainingNeuron())
            neuron = new Perceptron(dataPreparer.getWeights(), learningRate, bias, chooseActivateFunction());
        else
            neuron = new Adaline(dataPreparer.getWeights(), learningRate, bias, chooseActivateFunction());
    }

    private void prepareTestData() {
        int amount = Integer.valueOf(testInputAmount.getText());
        dataPreparer.initData(amount);
        double[][] inputData = dataPreparer.getInput();
        addDataToChar(inputData);
    }

    private void addDataToChar(double[][] inputData) {
        chart.getData().remove(0);
        dataSeries = new ScatterChart.Series();
        dataSeries.setName("Test Data");
        for (int i = 0; i < inputData.length; i++) {
            dataSeries.getData().add(new ScatterChart.Data<>(inputData[i][0],inputData[i][1]));
        }
        chart.getData().add(0, dataSeries);
    }

    private void randomWeigths() {
        double startValue = Double.valueOf(startTextField.getText().replace(',', '.'));
        double endValue = Double.valueOf(endTextField.getText().replace(',', '.'));
        double bias = Double.valueOf(biasTextField.getText().replace(',', '.'));
        dataPreparer.randomWeights(startValue, endValue);
        double[] weights = dataPreparer.getWeights();
        addWeightsToChar(weights, bias);
    }

    private void addWeightsToChar(double[] weights, double bias) {
        chart.getData().remove(1);
        weightsSeries = new LineChart.Series();
        weightsSeries.setName("Weights Line");

        weightsSeries.getData().add(new XYChart.Data<>(-1, ((weights[0] - bias) / weights[1])  ));
        weightsSeries.getData().add(new XYChart.Data<>(1.1, ((-weights[0]*1.1 - bias) / weights[1]) ));

        chart.getData().add(1, weightsSeries);
    }

    private boolean chooseActivateFunction() {
        return unipolarRadioButton.isSelected();
    }

    private boolean chooseTrainingNeuron() {
        return perceptronRadioButton.isSelected();
    }

    private void addToTableView(double[][] input, int[] output){
        List<TableViewData> tableViewDatas = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {
            tableViewDatas.add(new TableViewData(input[i][0],input[i][1],output[i]));
        }
        ObservableList<TableViewData> data = FXCollections.observableArrayList(tableViewDatas);

        x1Column.setCellValueFactory( new PropertyValueFactory<TableViewData, Double>("x1"));
        x2Column.setCellValueFactory( new PropertyValueFactory<TableViewData, Double>("x2"));
        yColumn.setCellValueFactory( new PropertyValueFactory<TableViewData, Double>("y"));
        tableView.setItems(data);
    }

    public static  class TableViewData{
        SimpleDoubleProperty x1;
        SimpleDoubleProperty x2;
        SimpleIntegerProperty y;

        public TableViewData(double x1, double x2, int y) {
            this.x1 = new SimpleDoubleProperty(x1);
            this.x2 = new SimpleDoubleProperty(x2);;
            this.y = new SimpleIntegerProperty(y);
        }

        public double getX1() {
            return x1.get();
        }
        public void setX1(double x1) {
            this.x1.set(x1);
        }

        public double getX2() {
            return x2.get();
        }

        public void setX2(double x2) {
            this.x2.set(x2);
        }

        public int getY() {
            return y.get();
        }

        public void setY(int y) {
            this.y.set(y);
        }
    }

}
