package ca.nait.dmit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BMI {
    private double weight;
    private double height;

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
