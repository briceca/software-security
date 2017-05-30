package husq;

/**
 * Created by brice on 30/05/2017.
 */
public class SendObject {
    long timestampSnd;
    long timestampRes;
    int var;
    boolean status;
    String systemName;
    long delay;
    String uuid;
    int version;

    SendObject(String uuid, int version, String systemName){
        this.uuid = uuid;
        this.version = version;
        this.systemName = systemName;
        timestampSnd = System.currentTimeMillis();
        var = (int)(Math.random() * 50);
        status = false;
    }
    SendObject(String uuid, int version, String systemName, boolean status, int var,long timestampRes, long timestampSnd ){
        this.uuid = uuid;
        this.version = version;
        this.systemName = systemName;
        this.timestampSnd = timestampSnd;
        this.timestampRes = timestampRes;
        this.var = var;
        this.status = status;
        delay = timestampRes-timestampSnd;
    }

    public long getTimestampSnd() {

        return timestampSnd;
    }

    public void setTimestampSnd(long timestampSnd) {
        this.timestampSnd = timestampSnd;
    }

    public long getTimestampRes() {
        return timestampRes;
    }

    public void setTimestampRes(long timestampRes) {
        this.timestampRes = timestampRes;
    }

    public int getVar() {
        return var;
    }

    public boolean getStatus(){
        return status;
    }

    public void setVar(int var) {
        this.var = var;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
