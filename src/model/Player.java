package model;

import java.util.ArrayList;
import java.util.List;

//************************************************
//Brennon Ary
//ITEC 3860
//11/11/2025
//Player Script
//************************************************
public class Player {

    private int currentRoom;
    private String name;
    private List<Item> inventory;
    private Item equippedItem;
    private int health;
    private int baseAttack;
    private int currentAttack;

    private Equipment equipment;

    public Player(String name) {
        this.name = name;
        this.currentRoom = 1;
        this.health = 100;
        this.baseAttack = 10;
        this.currentAttack = baseAttack;
        this.equipment = new Equipment();
        this.inventory = new ArrayList<>();
    }

    // ---- Core Stat Updates ----
    private void updateAttack() {
        this.currentAttack = baseAttack + equipment.getWeaponAttackBonus();
    }

    public int getCurrentAttack() {
        updateAttack();
        return currentAttack;
    }

    public void heal(int amount) {
        this.health = Math.min(100, this.health + amount);
    }

    public void applyTempAttackBoost(int amount) {
        this.currentAttack += amount;
    }

    // ---- Inventory Tools ----
    public Item findInInventory(String idOrName) {
        if (inventory == null) return null;
        for (Item item : inventory) {
            if (item.getItemID().equalsIgnoreCase(idOrName)
                    || item.getItemName().equalsIgnoreCase(idOrName)) {
                return item;
            }
        }
        return null;
    }

    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        System.out.println("=== INVENTORY ===");
        for (Item i : inventory) {
            System.out.println("- [" + i.getItemName() + "]: " + i.getDescription());
        }
    }

    // ---- Item Handling ----
    public void grabItem(String itemName) {
        Room room = DatabaseManager.getRoom(this.currentRoom);
        Item found = null;

        for (Item i : room.getItems()) {
            if (i.getItemName().equalsIgnoreCase(itemName)) {
                found = i;
                break;
            }
        }

        if (found == null) {
            System.out.println("There is no item with that name here.");
            return;
        }

        room.removeItem(found);
        inventory.add(found);
        System.out.println("You picked up: " + found.getItemName());
    }

    public void dropItem(String itemName) {
        Item found = findInInventory(itemName);
        if (found == null) {
            System.out.println("You don't have that item.");
            return;
        }

        inventory.remove(found);
        DatabaseManager.getRoom(this.currentRoom).addItem(found);

        System.out.println("You dropped: " + found.getItemName());
    }

    // ---- Equip & Use ----
    public void equip(Item item) {
        if (item == null) {
            System.out.println("Item not found in inventory.");
            return;
        }
        if (item instanceof WeaponItem) {
            equipment.equipWeapon((WeaponItem) item);
            equippedItem = item;
            updateAttack();
            System.out.println("Equipped weapon: " + item.getItemName());
        }
        else if (item instanceof ArmorItem) {
            equipment.equipArmor((ArmorItem) item);
            equippedItem = item;
            System.out.println("Equipped armor: " + item.getItemName());
        }
        else {
            System.out.println("You cannot equip this item.");
        }
    }

    public void unequip() {
        if (equippedItem == null) {
            System.out.println("Nothing is currently equipped.");
            return;
        }
        // Determine if item is weapon or armor
        if (equippedItem instanceof WeaponItem) {
            System.out.println("Unequipped weapon: " + equippedItem.getName());
            equipment.unequipWeapon();
        }
        else if (equippedItem instanceof ArmorItem) {
            ArmorItem armor = (ArmorItem) equippedItem;
            System.out.println("Unequipped armor: " + equippedItem.getName() + " (Slot: " + armor.getSlot() + ")");
            equipment.unequipArmor(armor.getSlot());
        }

        equippedItem = null;
        updateAttack();
    }

    public void heal(Item item) {
        if (!(item instanceof ConsumableItem consumable)) {
            System.out.println("You cannot use this item.");
            return;
        }
        if (consumable.use(this)) {
            System.out.println("Used: " + item.getItemName());
        } else {
            System.out.println("No uses remaining.");
        }
    }

    public void stats() {
        System.out.println("=== PLAYER STATS ===");
        System.out.println("HP: " + health);
        System.out.println("Attack: " + getCurrentAttack());
        if (equippedItem != null)
            System.out.println("Equipped: " + equippedItem.getItemName());
        else
            System.out.println("Equipped: None");
    }

    public List<Item> getInventory() { return inventory; }
    public int getRoom() { return currentRoom; }
    public void setRoom(int room) { this.currentRoom = room; }
    public int getHealth() { return health; }
    public void setHealth(int h) { this.health = Math.max(0, h); }
    public Equipment getEquipment() {
        return equipment;
    }
    public int getAttack() {
        return baseAttack + equipment.getWeaponAttackBonus();
    }
}