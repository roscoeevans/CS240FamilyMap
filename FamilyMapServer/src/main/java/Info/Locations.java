package Info;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Random;

public class Locations {
     Location[] data;

    public Locations(String filePath) {
        Gson gson = new Gson();

        try {
            Reader reader = new FileReader(filePath);
            Locations locData = gson.fromJson(reader, Locations.class);
            this.data = locData.getData();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    public Location getRandomLocation() {
        int size = data.length;
        Random rand = new Random();

        int index = rand.nextInt(size);
        return data[index];
    }

    public Location[] getData() {
        return data;
    }
}
