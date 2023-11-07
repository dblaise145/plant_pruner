import java.time.LocalDate;
import java.io.IOException;

public class Plant {
    String sname;
    String cname;
    String image;
    String prune_info;
    LocalDate last_pruned;

    public Plant(String scientific_name, String common_name, String image_link, String pruning_info, int[] last_pruned_array) throws IOException  {
        sname = scientific_name;
        cname = common_name;
        image = image_link;
        prune_info = pruning_info;
        last_pruned = LocalDate.of(last_pruned_array[0], last_pruned_array[1], last_pruned_array[2]); 
    }

    @Override
    public String toString() {
        return "\n" + sname + "\n" + cname + "\n\n\nPruning info: " + prune_info + "\n\nLast pruned: " + last_pruned + "\n";
    }
}
