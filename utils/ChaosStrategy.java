package utils;

import java.util.Random;

public class ChaosStrategy
{
//// I use a static inner class to implement my current best singleton pattern thread-safe implementation
    /*
     * Use static inner classes to implement the current best singleton pattern thread-safe implementation I use

     */
    private ChaosStrategy()
    {
        chaosvalue = 0.0;
        u=3.9999;//Complete chaos
    }

    private double u;

    private static class ChaosStrategyInner
    {
        private static ChaosStrategy instance= new ChaosStrategy();
    }

    public static ChaosStrategy getInstance()
    {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ChaosStrategyInner.instance;
    }

    private double chaosvalue;
    public double getChaosValue()
    {
        return chaosvalue;
    }

    public void setChaosValue(double value)
    {
        this.chaosvalue = value;
    }
 // Calculate the impact of the initial value, iterate 500 times to get the chaotic state value, which is generated based on Logistic mapping
    public void CalChaos()
    {
        Random rd = new Random();
        double chaosvalue = LM(500,rd.nextDouble());
        setChaosValue(chaosvalue);
    }

    public double LM(int n,double x0)
    {
        double result = x0;//Initial value of iteration
        for(int i=0;i<n;i++)//N iterations
        {
            result = Calculator.mul(Calculator.mul(Calculator.sub(1.0, result), result), u);
        }
        setChaosValue(result);
        return result;
    }

    public double PLM(int n,double x0)
    {
        double result = x0;//
        for(int i=0;i<n;i++)//
        {
            if(result>=0.0&&result<=0.5)
                result = Calculator.mul(Calculator.mul(Calculator.sub(0.5, result), result), u);
            if(result>=0.5&&result<=1.0)
                result = 1.0-Calculator.mul(Calculator.mul(Calculator.sub(1.0, result), Calculator.sub(result,0.5)), u);
        }
        setChaosValue(result);
        return result;
    }
}
