package multimedia.taskmanager;

import com.google.gson.*; 
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * xrhsimopoioume ton adapter gia na metatrepoume to LocalDate se JSON kai antistrofa
 * xrhsimopoieitai pano sta JsonSerializer kai JsonDeserializer apo tin vivliothiki Gson.
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    /**
     * @param src to LocalDate pou theloume na metatrepoume se JSON
     * @param context Το Gson Serialization Context
     * @return to JSON element pou antistoixei sto LocalDate
     */
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString()); // metatrepei to LocalDate se simvoloseira
    }

    /**
     * @param json to json element pou periexei tin hmeromia ws string
     * @return to anikeimeno pou proekipse 
     * @throws JsonParseException an h hmerominia den einai se swsti morfi
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString()); // metatrepei to json se LocalDate
    }
}
