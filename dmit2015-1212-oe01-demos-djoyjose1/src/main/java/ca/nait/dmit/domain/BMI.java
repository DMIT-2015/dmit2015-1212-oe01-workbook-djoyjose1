package ca.nait.dmit.domain;

public class BMI {
    private double weight;
    private double height;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public BMI() {
        this.weight = 100;
        this.height = 60;
    }

    public BMI(double weight, double height) {
        this.weight = weight;
        this.height = height;
    }

    public double bmi() {
        return 703 * weight / Math.pow(height, 2);
    }

    public String bmiCategory() {
        String category = "underweight";
        double bmiValue = bmi();

        if (bmiValue >= 18.5 && bmiValue <= 24.9)
            category = "normal";
        else if (bmiValue >= 25 && bmiValue <= 29.9)
            category = "overweight";
        else if (bmiValue >= 30)
            category = "obese";

        return category;
    }
}
