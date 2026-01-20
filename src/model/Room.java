package model;

import java.util.ArrayList;
import java.util.List;

//Author: Joey Chen
public class Room {
    public int id;
    public String name;
    public String description;
    public int north;
    public int south;
    public int east;
    public int west;
    public int monster;
    public int puzzle;

    public Room(int id, String name, String description, int north, int south, int east, int west, int monster, int puzzle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.monster = monster;
        this.puzzle = puzzle;
    }

    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
