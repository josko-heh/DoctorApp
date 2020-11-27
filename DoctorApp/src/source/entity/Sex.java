package source.entity;

public class Sex {
    private final Integer id;
    private String gender;

    public Sex(Integer id, String gender) {
        this.id = id;
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return gender;
    }
}