package by.mluvse.javabot.service;

import by.mluvse.javabot.enums.ActivityType;
import by.mluvse.javabot.enums.Goal;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RationCalculateService {
    private static final double KCAL_PER_STEP= 0.0005;

    public double calculateBmr(Map<String,Object> data){
        double weight=toDouble(data.get("weight"));
        double height= toDouble(data.get("height"));
        int age= toInteger(data.get("age"));
        boolean isFemale= "FEMALE".equals(data.get("gender"));
        return isFemale
                ? 10 * weight + 6.25 * height - 5 * age - 161
                : 10 * weight + 6.25 * height - 5 * age +5;
    }

    public double calculateTdee(Map<String,Object> data){
        double bmr= calculateBmr(data);
        ActivityType activityType= ActivityType.valueOf(data.get("activityType").toString());
        double tdee= bmr* activityType.getBaseCoefficient();

        if(activityType==ActivityType.STEPS||activityType==ActivityType.MIXED){
            int steps= toInteger(data.get("steps"));
            double weight= toDouble(data.get("weight"));
            tdee+=steps*KCAL_PER_STEP*weight;
        }

        if(activityType==ActivityType.STRENGTH||activityType==ActivityType.MIXED){
            int coefficient= toInteger(data.get("restCoefficient"));
            int time=toInteger(data.get("workoutDuration"));
            int quantity=toInteger(data.get("workoutsPerWeek"));
            double weight= toDouble(data.get("weight"));
            double kcalPerWorkout=coefficient*weight*time;
            tdee+=(kcalPerWorkout*(double) quantity) /7;
        }
        return tdee;
    }

    public double calculateByTarget(double tdee, Goal goal){
        return tdee+goal.getCalorieDelta();
    }

    public double toDouble(Object value){
        return Double.parseDouble(value.toString().replace(",","."));
    }

    public int toInteger(Object value){
        return Integer.parseInt(value.toString());
    }
}
