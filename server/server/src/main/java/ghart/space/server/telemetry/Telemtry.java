package ghart.space.server.telemetry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@JsonComponent
public class Telemtry {
    
    // we expect a telemtry object to look something like the following
    // Any amount of tags and fields may be present
    // {
    //     "timeStamp": "2020-01-01T00:00:00Z",
    //     "tags": [
    //         {
    //             "name": "make" ,
    //             "value": "honda"
    //         }
    //     ],
    //     "fields": [
    //         {
    //             "name": "rpm" ,
    //             "type": "long",
    //             "value": 2000
    //         },
    //         {
    //             "name" : "throttlePos",
    //             "type": "float",
    //             "value": 0.2
    //         }
    //     ]
    // }

    // Record to hold the data of an influxdb tag from our telemtry object
    @Embeddable
    public record Tag(
                    String name, 
                    @Column(name = "value") String val){};

    // record to hold data of an influxdb field from our telemtry object
    // later we want to make this generic
    // private record Field<T>(String name, String type, T value){};
    @Embeddable
    public record Field(
                        String name,
                        String type, 
                        @Column(name = "value") long val){};

    @ElementCollection
    private List<Tag> tags;
    @ElementCollection
    private List<Field> fields;
    @Id
    private String timeStamp;


    public Telemtry(){}

    public Telemtry(String timeStamp, List<Tag> tags, List<Field> fields){
        this.timeStamp = timeStamp;
        this.tags = tags;
        this.fields = fields;
    }

    public List<Tag> getTags(){
        return this.tags;
    }

    public List<Field> getFields(){
        return this.fields;
    }

    public String getTimeStamp(){
        return this.timeStamp;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Time Stamp: " + timeStamp + " Tags: " + tags + " Fields: " + fields;
    }

    /**
     * Overrive the default deserializer behavior. 
     */
	public static class Deserializer extends JsonObjectDeserializer<Telemtry> {

        @Override
        protected Telemtry deserializeObject(com.fasterxml.jackson.core.JsonParser jsonParser,
                DeserializationContext context, ObjectCodec codec, JsonNode tree) throws IOException {
                    
                List<Tag> tags = new ArrayList<>();
                List<Field> fields = new ArrayList<>();

                System.out.println("inside deserialize");

                // grab the time stamp
                String timeStamp = nullSafeValue(tree.get("timeStamp"), String.class);
                System.out.println("Time stamp: " + timeStamp);

                // grab the list of all tags
                for(Iterator<JsonNode> i = tree.get("tags").elements(); i.hasNext();){
                    // grab the next tag as a json node and parse it
                    JsonNode node = i.next();
                    Tag tag = new Tag(
                        nullSafeValue(node.get("name"), String.class),
                        nullSafeValue(node.get("value"), String.class)
                    );
                    tags.add(tag);
                }

                // grab the list of all fields
                for(Iterator<JsonNode> i = tree.get("fields").elements(); i.hasNext();){
                    // grab the next field as a json node and parse it
                    JsonNode node = i.next();
                    Field field = new Field(
                        nullSafeValue(node.get("name"), String.class),
                        nullSafeValue(node.get("type"), String.class),
                        nullSafeValue(node.get("value"), Long.class)
                    );
                    fields.add(field);
                }

                
                System.out.println("tags " + tags);
                System.out.println("fields " + fields);

                return new Telemtry(timeStamp, tags, fields);
        }

	}

}

