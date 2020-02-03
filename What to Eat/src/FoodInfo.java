import java.io.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Scanner;

public class FoodInfo {

    static HashMap<String, double[]> info;
    static HashMap<String, double[][]> storage;
    static int nutritionLength;
    static String[] nutritionNames;

    public static void readInfo(File file) throws FileNotFoundException {
        info = new HashMap<>();
        Scanner sc = new Scanner(file);
        nutritionNames = sc.nextLine().split(" ");
        nutritionLength = nutritionNames.length;
        while(sc.hasNextLine()){
            String[] infoLine = sc.nextLine().split(" ");
            String name = infoLine[0];
            double[] data = new double[infoLine.length-1];
            for(int i = 1;i<infoLine.length;i++){
                data[i-1] = Double.parseDouble(infoLine[i]);
            }
            info.put(name,data);
        }

    }

    public static void readExisting(File file) throws FileNotFoundException {
        storage = new HashMap<>();
        Scanner sc = new Scanner(file);
        LocalDate today = LocalDate.now();
        while(sc.hasNextLine()){
            String[] infoLine = sc.nextLine().split(" ");
            if(!infoLine[0].equals("")) {
                String name = infoLine[0];
                LocalDate dateBought = LocalDate.parse(infoLine[1]);
                double numBought = Double.parseDouble(infoLine[2]);
                double daysBetween = (double) ChronoUnit.DAYS.between(dateBought, today);
                if (!storage.containsKey(name)) {
                    double[][] data = new double[1][];
                    data[0] = new double[2];
                    data[0][0] = daysBetween;
                    data[0][1] = numBought;
                    storage.put(name, data);
                } else {
                    double[][] data = storage.get(name);
                    boolean inside = false;
                    for (int i = 0; i < data.length; i++) {
                        if (daysBetween == data[i][0]) {
                            data[i][1] += numBought;
                            inside = true;
                            break;
                        }
                    }
                    if (inside == false) {
                        double[][] newData = new double[data.length + 1][];
                        for (int j = 0; j < data.length; j++) {
                            newData[j] = data[j];
                        }
                        double[] newEntry = new double[2];
                        newEntry[0] = daysBetween;
                        newEntry[1] = numBought;
                        newData[data.length] = newEntry;
                        storage.replace(name, newData);
                    }
                }
            }

        }
    }


    public static void readOnDay(File file, LocalDate date) throws FileNotFoundException {
        File existingFile = new File("src/existingFood.txt");
        readExisting(existingFile);
        Scanner sc = new Scanner(file);
        LocalDate today = LocalDate.now();
        double daysBetween = (double)ChronoUnit.DAYS.between(date, today);
        while(sc.hasNextLine()){
            String[] infoLine = sc.nextLine().split(" ");
            String name = infoLine[0];
            double numBought = Double.parseDouble(infoLine[1]);
            if(!storage.containsKey(name)){
                double[][] data = new double[1][];
                data[0] = new double[2];
                data[0][0] = daysBetween;
                data[0][1] = numBought;
                storage.put(name, data);
            }
            else{
                double[][] data = storage.get(name);
                boolean inside = false;
                for(int i = 0;i<data.length;i++){
                    if(daysBetween == data[i][0]){
                        data[i][1]+= numBought;
                        inside = true;
                        break;
                    }
                }
                if(inside ==false) {
                    double[][] newData = new double[data.length + 1][];
                    for (int j = 0; j < data.length; j++) {
                        newData[j] = data[j];
                    }
                    double[] newEntry = new double[2];
                    newEntry[0] = daysBetween;
                    newEntry[1] = numBought;
                    newData[data.length] = newEntry;
                    storage.replace(name,newData);
                }
            }

        }

    }

    public static void writeStorage(File file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        LocalDate today = LocalDate.now();
        for(String s: storage.keySet()){
            for(int i =0; i<storage.get(s).length; i++){
                double[] dataI = storage.get(s)[i];
                LocalDate boughtDate = today.minusDays((long)dataI[0]);
                writer.println(s+" "+boughtDate.toString()+ " "+ Double.toString(dataI[1]));
            }
        }
        writer.close();
    }


}
