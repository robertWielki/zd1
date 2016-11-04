package com.robert.NeuralNetwork.algorithm;


public class Adaline implements Neuron {


    private double[] weights;
    private double learningRate;
    private double bias;
    private boolean isUnipolarFunction;

    public Adaline(double[] weights, double learningRate, double bias, boolean isUnipolarFunction) {
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
        return sum + bias;
    }

    @Override
    public int activationFunction(double[] input){
        return sum(input) >= 0 ? 1 : 0;
    }

    @Override
    public double learn(double[] input, int output){
        double s = activationFunction(input);
        double error = output - s;
        calcNewWeight(error, input);
        return error * error;
    }

    private void calcNewWeight(double error, double[] input) {
        for (int j = 0; j < weights.length; j++) {
            weights[j] += error * learningRate * input[j];
        }
        bias += learningRate * error;
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
