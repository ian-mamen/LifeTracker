import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;

public class Ranger implements Rangers {

    private String name;
    private String contactInfo;
    private int id;

    public Ranger(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return contactInfo;
    }
    @Override
    public boolean equals(Object otherRanger){
        if (!(otherRanger instanceof Ranger)) {
            return false;
        } else {
            Ranger newRanger = (Ranger) otherRanger;
            return this.getName().equals(newRanger.getName()) &&
                    this.getDescription().equals(newRanger.getDescription());
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO rangers (name, contactInfo) VALUES (:name, :contactInfo)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("contactInfo", this.contactInfo)
                    .executeUpdate()
                    .getKey();
        }
    }
    public static List<Ranger> all() {
        String sql = "SELECT * FROM rangers";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Ranger.class);
        }
    }
    public int getId() {
        return id;
    }

    public void delete() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM rangers WHERE id = :id;";
            con.createQuery(sql)
                    .addParameter("id", this.id)
                    .executeUpdate();
            String joinDeleteQuery = "DELETE FROM rangers_sightings WHERE ranger_id = :rangerId";
            con.createQuery(joinDeleteQuery)
                    .addParameter("rangerId", this.getId())
                    .executeUpdate();
        }
    }

    public void addSighting(Sighting sighting) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO rangers_sightings (ranger_id, sighting_id) VALUES (:ranger_id, :sighting_id)";
            con.createQuery(sql)
                    .addParameter("ranger_id", this.getId())
                    .addParameter("sighting_id", sighting.getId())
                    .executeUpdate();
        }
    }

    public static Ranger find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM rangers where id=:id";
            Ranger ranger = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Ranger.class);
            return ranger;
        }
    }

    public List<Sighting> getSightings() {
        try(Connection con = DB.sql2o.open()){
            String joinQuery = "SELECT sighting_id FROM rangers_sightings WHERE ranger_id = :ranger_id";
            List<Integer> sightingIds = con.createQuery(joinQuery)
                    .addParameter("ranger_id", this.getId())
                    .executeAndFetch(Integer.class);

            List<Sighting> sightings = new ArrayList<Sighting>();

            for (Integer sightingId : sightingIds) {
                String sightingQuery = "SELECT * FROM sightings WHERE id = :sightingId";
                Sighting Sighting = con.createQuery(sightingQuery)
                        .addParameter("sightingId", sightingId)
                        .executeAndFetchFirst(Sighting.class);
                sightings.add(Sighting);
            }
            return sightings;
        }

    }
}
