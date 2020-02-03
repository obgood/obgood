import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Menu {
    static double[] dailyIntake = new double[FoodInfo.nutritionLength];


    static HashMap<String, String[]> recipe;
    public static void parseRecipe(File file) throws FileNotFoundException {
        Menu.dailyIntake = new double[]{5, 5, 2};
        recipe = new HashMap<>();
        Scanner sc = new Scanner(file);
        while(sc.hasNextLine()){
            String[] infoLine = sc.nextLine().split(" ");
            String name = infoLine[0];
            String[] data = new String[infoLine.length-1];
            for(int i = 1;i<infoLine.length;i++){
                data[i-1] = infoLine[i];
            }
            recipe.put(name,data);
        }
        getTotalNutrition("tomatoFriedEggs");
    }

    public static double getCookingTime(String name){
        return Double.parseDouble(recipe.get(name)[0]);
    }

    public static double[] getTotalNutrition(String name){
        double[] nutrition = new double[FoodInfo.nutritionLength];
        String[] data = recipe.get(name);
        for(int i = 0; i<FoodInfo.nutritionLength;i++){
            String thisNutritionName = FoodInfo.nutritionNames[i];
            nutrition[i] =0;
            for(int j = 1; j<data.length;j++){
                String[] entry = data[j].split(":");
                String foodName = entry[0];
                double quantity = Double.parseDouble(entry[1]);
                nutrition[i]+=FoodInfo.info.get(foodName)[i]*quantity;
            }
        }

        return nutrition;
    }

    public static void generateDailyMenu(double time){
        HashSet<String> appeared = new HashSet<>();
        for (String dish1 : recipe.keySet()) {
            for (String dish2 : recipe.keySet()) {
                for (String dish3 : recipe.keySet()) {
                    if (!appeared.contains(dish1 + dish2 + dish3) && !appeared.contains(dish1 + dish3 + dish2)
                            && !appeared.contains(dish2 + dish1 + dish3) && !appeared.contains(dish2 + dish3 + dish1)
                            && !appeared.contains(dish3 + dish1 + dish2) && !appeared.contains(dish3 + dish2 + dish1)) {
                        appeared.add(dish1+dish2+dish3);
                        boolean inTime = getCookingTime(dish1) + getCookingTime(dish2) + getCookingTime(dish3) < time;
                        double[] totalNutrition = new double[FoodInfo.nutritionLength];
                        for (int i = 0; i < totalNutrition.length; i++) {
                            totalNutrition[i] = 0;
                            totalNutrition[i] += getTotalNutrition(dish1)[i];
                            totalNutrition[i] += getTotalNutrition(dish2)[i];
                            totalNutrition[i] += getTotalNutrition(dish3)[i];
                        }
                        boolean satisfiesNutrition = true;
                        for (int i = 0; i < totalNutrition.length; i++) {
                            if (totalNutrition[i] > 1.5 * dailyIntake[i] || totalNutrition[i] < 0.5 * dailyIntake[i]) {
                                satisfiesNutrition = false;
                                break;
                            }
                        }
                        if (inTime && satisfiesNutrition) {
                            System.out.println(dish1 + " " + dish2 + " " + dish3);
                        }

                    }

                }
            }
        }

    }
}


