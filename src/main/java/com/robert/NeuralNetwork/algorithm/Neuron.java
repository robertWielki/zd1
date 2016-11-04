package com.robert.NeuralNetwork.algorithm;

/**
 * Created by RLUKAS on 21.10.2016.
 */
public interface Neuron {
    double sum(double[] input);

    int activationFunction(double[] input);

    double learn(double[] input, int output);

    double[] getWeights();

    double getBias();
}
