package prefeitura.uaiot.br.uaiot.models;

/**
 * Created by AndreCoelho on 24/04/2018.
 */

public class Thing {

    private int id;

    private String name;

    private String description;

    private TypeThing typeThing;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeThing getTypeThing() {
        return typeThing;
    }

    public void setTypeThing(TypeThing typeThing) {
        this.typeThing = typeThing;
    }
}
