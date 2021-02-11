public class Visitor {
    public String name;
    public boolean isBoy;

    public boolean isBoy() {
        return isBoy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBoy(boolean boy) {
        isBoy = boy;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "name='" + name + '\'' +
                ", isBoy=" + isBoy +
                '}';
    }
}
