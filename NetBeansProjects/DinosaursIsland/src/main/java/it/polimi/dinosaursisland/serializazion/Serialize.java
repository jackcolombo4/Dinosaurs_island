package it.polimi.dinosaursisland.serializazion;

import it.polimi.dinosaursisland.partita.Game;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/**
 * Classe Serialize per serializzare su un file.
 */

public class Serialize implements Serializable {

    public String serializeIt   (Object object) {

        try {
            if (object instanceof Game) {
                Game serializedGame = ((Game) object);
                serializedGame.endGame();

            }
            FileOutputStream out = new FileOutputStream("mappaDino");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(object);
            oos.close();

            return "partita1";

        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
