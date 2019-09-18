package Info;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Random;

public class Names {
    private String[] data;

    public Names(String filePath) {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(filePath);
            Names locData = gson.fromJson(reader, Names.class);
            this.data = locData.getData();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public String[] getData() {
        return data;
    }
    public String getRandomName() {
        int size = data.length;
        Random rand = new Random();

        int index = rand.nextInt(size);
        return data[index];
    }


}
