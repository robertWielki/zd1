package com.robert.NeuralNetwork.algorithm;

/**
 * Created by RLUKAS on 15.10.2016.
 */
public class Perceptron implements Neuron{

    private double[] weights;
    private double learningRate;
    private double bias;
    private boolean isUnipolarFunction;

    public Perceptron(double[] weights, double learningRate, double bias, boolean isUnipolarFunction) {
        this.weights = weights;
        this.learningRate = learningRate;
        this.bias = bias;
        this.isUnipolarFunction = isUnipolarFunction;
    }

    @Override
    public double sum(double[] input){
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += weights[i]*input[i];
        }
        return sum+bias;
    }

    @Override
    public int activationFunction(double[] input){
        if(isUnipolarFunction) {
            return sum(input) >= 0 ? 1 : 0;
        }else
            return sum(input) >= bias ? 1: -1;
    }

    @Override
    public double learn(double[] input, int output){
        int y = activationFunction(input);
        int error = output - y;
        calcNewWeight(error, input);
        return Math.abs(error);
    }

    private void calcNewWeight(int error, double[] input) {
        for (int j = 0; j < weights.length; j++) {
            weights[j] += error * learningRate * input[j];
        }
        bias = bias + learningRate*error;
    }

    @Override
    public double[] getWeights() {
        return weights;
    }

    @Override
    public double getBias() {
        return bias;
    }
}
