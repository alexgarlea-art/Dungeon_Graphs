public class Distance {
    private String prev;
    private float distance;

    public Distance(String prev, float dist){
        this.prev = prev;
        this.distance = dist;
    }

    public String getPrev() {
        return prev;
    }

    public float getDistance() {
        return distance;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
