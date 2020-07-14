import org.sql2o.Connection;

import java.util.List;

public class EndangeredAnimal {

    public boolean endangered;
    public String name;
    public int id;
    private String health;
    private String age;


    public EndangeredAnimal(String name, String health, String age) {
        this.name = name;
        this.id = id;
        this.health = health;
        this.age = age;
    }

    public String getName () {
        return name;
    }

    public int getId () {
        return id;
    }
    public String getHealth () {
        return health;
    }
    public String getAge () {
        return age;
    }
    @Override
    public boolean equals(Object otherEndangeredAnimal) {
        if (!(otherEndangeredAnimal instanceof EndangeredAnimal)) {
            return false;
        } else {
            EndangeredAnimal newEndangeredAnimal = (EndangeredAnimal) otherEndangeredAnimal;
            return this.getName().equals(newEndangeredAnimal.getName()) &&
                    this.getAge().equals(newEndangeredAnimal.getAge()) &&
                    this.getHealth().equals(newEndangeredAnimal.getHealth());
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO endangered_animal (name, health, age ) VALUES (:name, :health, :age)";
            this.id = (int)
                    con.createQuery(sql, true)
                            .addParameter("name", this.name)
                            .addParameter("age", this.age)
                            .addParameter("health", this.health)
                            .executeUpdate()
                            .getKey();
        }
    }
    public static List<EndangeredAnimal> all () {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM endangered_animals;";
            return con.createQuery(sql)
                    .executeAndFetch(EndangeredAnimal.class);
        }
    }

    public static EndangeredAnimal find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM endangered_animals WHERE id=:id;";
            EndangeredAnimal EndangeredAnimals = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(EndangeredAnimal.class);
            return EndangeredAnimals;
        }
    }

    public void updateHealth(String health) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "UPDATE endangered_animals SET health=:health WHERE id=:id;";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("health", health)
                    .executeUpdate();
        }
    }

    public void updateAge(String age) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "UPDATE endangered_animals SET age=:age WHERE id=:id;";
            con.createQuery(sql)
                    .addParameter("age", age)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }


}
