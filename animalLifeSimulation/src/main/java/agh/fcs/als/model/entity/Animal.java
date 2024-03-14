package agh.fcs.als.model.entity;

import agh.fcs.als.model.entity.utilities.AnimalConfig;
import agh.fcs.als.model.entity.utilities.Genome;
import agh.fcs.als.model.utilities.Direction;
import agh.fcs.als.model.utilities.MoveValidator;
import agh.fcs.als.model.utilities.Vector2d;

import java.util.*;

public class Animal {
    private final Random random = new Random();
    private final AnimalConfig config;
    private final Genome genome;
    private Vector2d position;
    private Direction direction;
    private int energy;
    private int age;
    private int grassEaten;
    private int childCount;

    public Animal(Vector2d position, Direction direction, int energy, int age, int grassEaten, int childCount, AnimalConfig config, Genome genome) {
        validateNotNull(config, "config");
        this.config = config;
        validateNotNull(genome, "genome");
        this.genome = genome;

        setPosition(position);
        setDirection(direction);
        setEnergy(energy);
        setAge(age);
        setGrassEaten(grassEaten);
        setChildCount(childCount);
    }

    public void move(MoveValidator moveValidator){
        if(moveValidator == null){
            throw new IllegalArgumentException("moveValidator cannot be null");
        }
        if(energy <= 0){
            throw new IllegalStateException("Animal cannot move with energy less than 1.");
        }
        int angle = genome.getActiveGeneAndUpdate();
        direction = direction.rotate(angle);
        Vector2d targetPosition = position.add(direction.toUnitVector());
        if(moveValidator.canMoveTo(targetPosition)){
            position = targetPosition;
        }else{
            direction = direction.turnAround();
            position = position.add(direction.toUnitVector());
        }
        energy--;
    }

    public Optional<Animal> reproduce(Animal other){
        if(other.getEnergy() > this.energy){
            throw new IllegalArgumentException("Second animal cannot have higher energy than the first one.");
        }
        if(!other.getPosition().equals(this.position)){
            throw new IllegalArgumentException("Only animals on the same position can reproduce.");
        }
        if(other.getEnergy() < config.energyNeededToBeReadyForReproduction()){
            return Optional.empty();
        }
        int mutationAmount = random.nextInt(config.minMutationNumber(), config.maxMutationNumber()+1);
        List<Integer> childGenesBefore = Genome.generateChildGenes(this.genome, this.energy, other.getGenome(), other.getEnergy());
        List<Integer> childGenesAfter = config.mutation().apply(childGenesBefore, mutationAmount);
        Animal child = createChild(childGenesAfter);
        return Optional.of(child);
    }

    public void eat(){}

    private Animal createChild(List<Integer> childGenes){
        return new Animal.Builder(this.config)
                .setEnergy(config.energyUsedForReproduction()*2)
                .setPosition(this.position)
                .setGenome(new Genome(childGenes))
                .build();
    }

    private static void validateNotNull(Object object, String name){
        if(object == null){
            throw  new IllegalArgumentException(name + " cannot be null.");
        }
    }
    public void setPosition(Vector2d position) {
        Animal.validateNotNull(position, "position");
        this.position = position;
    }

    public void setEnergy(int energy) {
        if(energy <= 0 || energy > config.maxEnergy()){
            throw new IllegalArgumentException("Energy must be positive and smaller than maxEnergy.");
        }
        this.energy = energy;
    }

    public void setAge(int age) {
        if(age<=0){
            throw new IllegalArgumentException("Age must be greater than 0.");
        }
        this.age = age;
    }

    public void setGrassEaten(int grassEaten) {
        if(grassEaten<0){
            throw new IllegalArgumentException("grassEaten must be positive.");
        }
        this.grassEaten = grassEaten;
    }

    public void setChildCount(int childCount) {
        if(childCount<0){
            throw new IllegalArgumentException("childCount must be positive.");
        }
        this.childCount = childCount;
    }

    public void setDirection(Direction direction) {
        validateNotNull(direction, "direction");
        this.direction = direction;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    public int getGrassEaten() {
        return grassEaten;
    }

    public int getChildCount() {
        return childCount;
    }

    public Genome getGenome(){
        return genome;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "position=" + position +
                ", direction=" + direction +
                ", energy=" + energy +
                ", age=" + age +
                ", grassEaten=" + grassEaten +
                ", childCount=" + childCount +
                ", config=" + config +
                ", genome=" + genome +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return energy == animal.energy
                && age == animal.age
                && direction.equals(animal.direction)
                && grassEaten == animal.grassEaten
                && childCount == animal.childCount
                && position.equals(animal.position)
                && config.equals(animal.config)
                && genome.equals(animal.genome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, direction, energy, age, grassEaten, childCount, config, genome);
    }

    public static class Builder{
        private final AnimalConfig config;
        private Genome genome;
        private Vector2d position = new Vector2d(0, 0);
        private Direction direction = Direction.N;
        private int energy;
        private int age = 1;
        private int grassEaten = 0;
        private int childCount = 0;

        public Builder(AnimalConfig config) {
            Animal.validateNotNull(config, "config");
            this.config = config;
            this.energy = config.startEnergy();
            this.genome = Genome.generate(config.genomeLength());
        }

        public Builder setPosition(Vector2d position) {
            this.position = position;
            return this;
        }

        public Builder setDirection(Direction direction){
            this.direction = direction;
            return this;
        }

        public Builder setEnergy(int energy) {
            this.energy = energy;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setGrassEaten(int grassEaten) {
            this.grassEaten = grassEaten;
            return this;
        }

        public Builder setChildCount(int childCount) {
            this.childCount = childCount;
            return this;
        }

        public Builder setGenome(Genome genome){
            validateNotNull(genome, "genome");
            this.genome = genome;
            return this;
        }

        public Animal build(){
            return new Animal(position, direction, energy, age, grassEaten, childCount, config, genome);
        }
    }
}
