import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {


        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }

        port(port);

        staticFileLocation("/public");
        String layout = "templates/layout.hbs";

        // routing the homepage

        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("animals", Animal.all());
            model.put("endangeredAnimal", EndangeredAnimal.all());
            model.put("sighting", Sighting.all());
            model.put("rangers", Ranger.all());
            model.put("template", "templates/index.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());


        // route to add new animal
        get("/animal/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("animals", Animal.all());
            model.put("endangeredAnimal", EndangeredAnimal.all());
            model.put("template", "templates/animal-form.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        // route to add new ranger
        get("/rangers/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.params("name");
            String contactInfo = request.params("contactInfo");
            Ranger ranger = new Ranger(name, contactInfo);
            ranger.save();
            model.put("template", "templates/ranger-form.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());



        //getting new Rangers
        get("/rangers", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("rangers", Ranger.all());
            model.put("template", "templates/rangers.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        // getting the endangered animals

        get("/endangeredAnimal/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String health = request.params("health");
            String age = request.params("age");
            String name = request.params("name");
            EndangeredAnimal endangeredAnimal = new EndangeredAnimal(name, health, age);
            endangeredAnimal.save();
            model.put("template", "templates/endangered-animals-form.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        get("/animals/:id", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String,Object>();
            Animal animal = Animal.find(Integer.parseInt(request.params(":id")));
            model.put("animal", animal);
            model.put("template", "templates/animals.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        get("/rangers/:id", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String,Object>();
            Ranger ranger = Ranger.find(Integer.parseInt(request.params(":id")));
            model.put("ranger", ranger);
            model.put("template", "templates/ranger.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        get("/rangers/:id/sightings/:id", (request, response) -> {
            HashMap<String, Object> model = new HashMap<String,Object>();
            Ranger ranger = Ranger.find(Integer.parseInt(request.params(":id")));
            Sighting sighting = Sighting.find(Integer.parseInt(request.params(":id")));
            model.put("ranger", ranger);
            model.put("template", "templates/ranger.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());


        //checking if animal is normal or endangered
        post("/animal/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String type = request.queryParams("type");

            System.out.println(type);
            if(type.equals("endangered"))
            {
                response.redirect("/endangeredAnimal/new");
            }
            else {
                String name = request.queryParams("name");
                Animal Animal = new Animal(name);
                Animal.save();
                model.put("animals", Animal.all());
                model.put("template", "templates/animals.hbs");
            }
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        post("/endangeredAnimals/new", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String health = request.params("health");
            String age = request.params("age");
            String name = request.params("name");
            EndangeredAnimal endangeredAnimals = new EndangeredAnimal(name, health, age);
            endangeredAnimals.save();
            model.put("endangeredAnimals", EndangeredAnimal.all());
            model.put("template", "templates/endangered-animals-success.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());

        post("/rangers", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.queryParams("name");
            String contactInfo = request.queryParams("contactInfo");
            Ranger newRanger = new Ranger(name, contactInfo);
            newRanger.save();
            model.put("template", "templates/rangers.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());


        post("/animals", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String name = request.queryParams("name");
            Animal newAnimal = new Animal(name);
            newAnimal.save();
            model.put("template", "templates/animals.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());


        post("/sightings", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            int animalId = Integer.parseInt(request.queryParams("animalId"));
            String location = request.queryParams("location");
            String rangerName = request.queryParams("rangerName");
            Sighting newSightings = new Sighting(location, animalId, rangerName);
            newSightings.save();
            model.put("template", "templates/sightings.hbs");
            return new ModelAndView(model, layout);
        }, new HandlebarsTemplateEngine());


    }
}
