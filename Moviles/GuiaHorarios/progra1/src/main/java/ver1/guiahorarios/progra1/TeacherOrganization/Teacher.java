package ver1.guiahorarios.progra1.TeacherOrganization;

/**
 * Created by sanchosv on 18/04/14.
 */
public class Teacher {

    public Teacher(){}

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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(int ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    @Override
    public String toString()
    {
        return "Nombre Completo: " + name + "\n" +"Calificación Promedio: " +averageRating;
    }

    private int id;
    private String name;
    private int ratingTotal;
    private double averageRating;


}
