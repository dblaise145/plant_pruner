import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
 

public class Plants {
    public static void main(String[] args) {
        ArrayList<Plant> plants = new ArrayList<Plant>();
        readCSV(plants);
        
        for (Plant plant : plants) {
            System.out.println(plant);
        }
    }

    public static void readCSV(ArrayList<Plant> plants){  
        plants.clear();
        String plant_info = "";
        try (BufferedReader br = new BufferedReader(new FileReader("plants.csv"))){
            br.readLine();
            while ((plant_info = br.readLine()) != null) 
            {  
                String[] pi = plant_info.split(",");
                int[] date = new int[3];
                for(int i = 0; i < 3; i++){
                    date[i] = Integer.parseInt(pi[i+4]);
                }
                Plant plant = new Plant(pi[0], pi[1], pi[2], pi[3], date);
                plants.add(plant); 
            }  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePruneInfo(int plantid, LocalDate newPruneInfo) {
        File csvFile = new File("plants.csv");
        File tempFile = new File("temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            int currentRow = 0;

            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(",");

                if (currentRow == plantid) {
                    cells[4] = String.valueOf(newPruneInfo.getYear());
                    cells[5] = String.valueOf(newPruneInfo.getMonthValue());
                    cells[6] = String.valueOf(newPruneInfo.getDayOfMonth());
                }

                String newLine = String.join(",", cells);
                writer.write(newLine);
                writer.newLine();

                currentRow++;
            }   
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the temp file
        if (!csvFile.delete()) {
            System.out.println("Could not delete original file");
        }
        if (!tempFile.renameTo(csvFile)) {
            System.out.println("Could not rename temp file");
        }
    }

    public static void addPlant(Plant newPlant) {
        File csvFile = new File("plants.csv");
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            String newLine = String.join(",",
                newPlant.sname,
                newPlant.cname,
                newPlant.image,
                newPlant.prune_info,
                String.valueOf(newPlant.last_pruned.getYear()),
                String.valueOf(newPlant.last_pruned.getMonthValue()),
                String.valueOf(newPlant.last_pruned.getDayOfMonth())
            );
    
            writer.write(newLine);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
