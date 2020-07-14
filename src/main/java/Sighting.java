import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;

public class Sighting {

    private String rangerName;
    private int animalId;
    private int id;

    public String getName(){
        return rangerName;
    }

    public int getId () {
        return id;
    }

    public int getAnimalId(){
        return animalId;
    }

    public Sighting(String location, int animalId, String rangerName) {
        this.rangerName = rangerName;
        this.animalId = animalId;
    }


    @Override
    public boolean equals(Object otherSighting){
        if (!(otherSighting instanceof Sighting)) {
            return false;
        } else {
            Sighting newSighting = (Sighting) otherSighting;
            return this.getName().equals(newSighting.getName()) &&
                    this.getAnimalId() == newSighting.getAnimalId();
        }
    }

    public static List<Sighting> all() {
        String sql = "SELECT * FROM sightings";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Sighting.class);
        }
    }
    public static Sighting find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM sightings where id=:id";
            Sighting sighting = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Sighting.class);
            return sighting;
        }
    }


    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO sightings (rangerName, animalId) VALUES (:rangerName, :animalId)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("rangerName", this.rangerName)
                    .addParameter("animalId", this.animalId)
                    .executeUpdate()
                    .getKey();

        }
    }
    public List<Ranger> getRangers() {
        try(Connection con = DB.sql2o.open()){
            String joinQuery = "SELECT ranger_id FROM rangers_sightings WHERE sighting_id = :sighting_id";
            List<Integer> rangerIds = con.createQuery(joinQuery)
                    .addParameter("sighting_id", this.getId())
                    .executeAndFetch(Integer.class);

            List<Ranger> rangers = new ArrayList<Ranger>();

            for (Integer rangerId : rangerIds) {
                String rangerQuery = "SELECT * FROM rangers WHERE id = :rangerId";
                Ranger ranger = con.createQuery(rangerQuery)
                        .addParameter("rangerId", rangerId)
                        .executeAndFetchFirst(Ranger.class);
                rangers.add(ranger);
            }
            return rangers;
        }
    }
}
