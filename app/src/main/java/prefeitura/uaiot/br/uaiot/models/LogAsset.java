package prefeitura.uaiot.br.uaiot.models;

import java.util.Date;

/**
 * Created by AndreCoelho on 24/04/2018.
 */

public class LogAsset {

    private int id;

    private Date time;

    private float latitude;

    private float longitude;

    private Thing thing;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }
}
