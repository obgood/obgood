import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Scanner;

public class Fridge {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        String in;
        File foodInfoFile = new File("src/foodInfo.txt");
        File existingFile = new File("src/existingFood.txt");
        File todayFile = new File("src/todayBought.txt");
        File recipeFile = new File("src/recipe.txt");
        while (true) {
            in = input.nextLine();
            if (in.equals("read info")){
                FoodInfo.readInfo(foodInfoFile);
                System.out.println("Done read info");
            } else if (in.equals("read existing")) {
                FoodInfo.readExisting(existingFile);
                System.out.println("Done read existing");
            } else if (in.equals("add today")) {
                LocalDate today = LocalDate.now();
                FoodInfo.readOnDay(todayFile, today);
                FoodInfo.writeStorage(existingFile);
                System.out.println("Done adding today's shopping");
            } else if (in.equals("read recipe")) {
                Menu.parseRecipe(recipeFile);
                System.out.println("finished reading recipe");
            } else if (in.equals("generate menu")) {
                FoodInfo.readInfo(foodInfoFile);
                FoodInfo.readExisting(existingFile);
                Menu.parseRecipe(recipeFile);
                Menu.generateDailyMenu(80);
                System.out.println("finished generating menu");

            }
        }
    }
}


