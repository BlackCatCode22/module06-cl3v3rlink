import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Animal {
    private static int animalCount = 0;
    private String species;
    private String name;
    private String sex;
    private int age;
    private String color;
    private int weight;
    private LocalDate birthDate;
    private LocalDate arrivalDate;
    private String habitat;

    public Animal(String species, String sex, int age, String color, int weight, LocalDate birthDate, LocalDate arrivalDate) {
        this.species = species;
        this.sex = sex;
        this.age = age;
        this.color = color;
        this.weight = weight;
        this.birthDate = birthDate;
        this.arrivalDate = arrivalDate;
        this.name = generateName();
        this.habitat = determineHabitat();
        animalCount++;
    }

    private String generateName() {
        String name = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("animalNames.txt"))) {
            String line;
            List<String> names = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }
            Random rand = new Random();
            name = names.get(rand.nextInt(names.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String determineHabitat() {
        // Implement logic to determine habitat based on species
        // For simplicity, returning species as habitat
        return species;
    }

    public String getDetails() {
        return String.format("%s; %s; birth date: %s; %s color; %s; %d pounds; arrived %s",
                name, species, birthDate, color, sex, weight, arrivalDate);
    }

    public String getHabitat() {
        return habitat;
    }

    public static int getAnimalCount() {
        return animalCount;
    }
}

class Zoo {
    private Map<String, List<Animal>> habitats;

    public Zoo() {
        habitats = new HashMap<>();
    }

    public void addAnimal(Animal animal) {
        String habitat = animal.getHabitat();
        habitats.putIfAbsent(habitat, new ArrayList<>());
        habitats.get(habitat).add(animal);
    }

    public void generateReport() {
        try (PrintWriter writer = new PrintWriter("zooPopulation.txt")) {
            for (String habitat : habitats.keySet()) {
                writer.println(habitat + " Habitat:");
                for (Animal animal : habitats.get(habitat)) {
                    writer.println(animal.getDetails());
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class ZookeepersChallenge {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        try (BufferedReader reader = new BufferedReader(new FileReader("arrivingAnimals.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println( Arrays.toString(parts)); // Debug statement
                String[] details = parts[0].split("\\s+");
                String species = details[details.length - 1];
                int age = Integer.parseInt(details[0]);
                String sex = details[2];
                String color = parts[2].trim();
                String[] weightPart = parts[3].trim().split("\\s+");
                System.out.println("Weight part: " + Arrays.toString(weightPart)); // Debug statement
                int weight = Integer.parseInt(weightPart[0]);
                LocalDate birthDate = genBirthDay(details);
                LocalDate arrivalDate = LocalDate.now();
                Animal animal = new Animal(species, sex, age, color, weight, birthDate, arrivalDate);
                zoo.addAnimal(animal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        zoo.generateReport();
    }

    private static LocalDate genBirthDay(String[] details) {
        int currentYear = LocalDate.now().getYear();
        int age = Integer.parseInt(details[0]);
        String[] birthPart = details[3].split("\\s+");
        String season = birthPart[birthPart.length - 1].toLowerCase();
        int birthYear = currentYear - age;
        switch (season) {
            case "spring":
                return LocalDate.of(birthYear, 2, 14);
            case "summer":
                return LocalDate.of(birthYear, 6, 27);
            case "autumn":
                return LocalDate.of(birthYear, 11, 5);
            case "winter":
                return LocalDate.of(birthYear, 1, 22);
            default:
                // If season is not provided, return default birthdate (spring)
                return LocalDate.of(birthYear, 6, 5);
        }
    }

}