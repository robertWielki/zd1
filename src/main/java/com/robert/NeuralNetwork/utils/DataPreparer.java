package com.robert.NeuralNetwork.utils;

import java.util.*;

/**
 * Created by RLUKAS on 15.10.2016.
 */
public class DataPreparer {

    private double[][] input;
    private int[] output;
    private double[] weights;

    public DataPreparer(){}

    public void initData(int size) {
        input = new double[size][2];
        output = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                input[i][0] = 0 + (random.nextDouble()) * (0.49); //random.nextDouble();
                input[i][1] =  0 + (random.nextDouble()) * (0.49);
                output[i] = (input[i][0] >= 0.5 && input[i][1]>= 0.5 )  ? 1 : 0;
            }
            else {
                input[i][0] = 0.6 + (random.nextDouble()) * (0.4); //random.nextDouble();
                input[i][1] =  0.6 + (random.nextDouble()) * (0.4);
                output[i] = (input[i][0] >= 0.5 && input[i][1]>= 0.5 )  ? 1 : 0;
            }
        }
    }
//
    public void randomWeights(double min, double max){
        weights = new double[2];
        Random random = new Random();
        for (int i = 0; i < weights.length; i++)
            weights[i] = min + (random.nextDouble()) * (max-min);
    }

    public double[][] getInput() {
        return input;
    }

    public int[] getOutput() {
        return output;
    }

    public double[] getWeights() { return weights; }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }
}
