package model;

//************************************************
//Brennon Ary
//ITEC 3860
//11/11/2025
//Combat script
//This function is called if the player enters a space with a monster.
//************************************************

import java.util.Scanner;

public class Battle {

    public static void startBattle(Player player, DatabaseManager.MonsterData monsterData, Scanner scanner) {
        String monsterID = "M" + monsterData.id;
        String name = monsterData.name;
        int hp = monsterData.hp;
        int attackMin = monsterData.attackMin;
        int attackMax = monsterData.attackMax;
        int defense = monsterData.defense;
        String description = "A hostile creature.";
        String location = "R" + player.getRoom();
        String drops = null;

        Monster monster = new Monster(monsterID, name, description, hp, attackMin, attackMax, defense, location, drops);

        System.out.println("\n=== BATTLE START ===");
        System.out.println("Enemy: " + monster.getName());
        System.out.println("Your HP: " + player.getHealth() + "/100   Enemy HP: " + monster.getHealth() + "/" + monster.getMaxHealth());
        System.out.println();

        battle(player, monster, scanner);

        if (player.getHealth() > 0 && monster.getHealth() <= 0) {
            System.out.println("\n=== VICTORY ===");
            System.out.println("You have defeated " + monster.getName() + "!");
        }

        for (String dropId : monsterData.drops) {
            DatabaseManager.ItemData data = DatabaseManager.getItem(dropId);
            if (data != null) {
                Item item = DatabaseManager.createItemFromData(data);
                if (item != null) {
                    player.getInventory().add(item);
                    System.out.println("You received: " + item.getName());
                }
            }
        }
    }

    public static void battle(Player player, Monster monster, Scanner scanner) {
        System.out.println("\n=== COMBAT START ===");
        System.out.println("A " + monster.getName() + " appears!");
        System.out.println("You attack first.");

        int previousRoom = player.getRoom();
        while (player.getHealth() > 0 && monster.getHealth() > 0) {
            System.out.println("\nYour HP: " + player.getHealth() + " | " + monster.getName() + " HP: " + monster.getHealth());
            System.out.print("Choose: ATTACK, USE, FLEE, HEAL, INVENTORY: ");

            String cmd = scanner.nextLine().trim().toUpperCase();
            boolean playerActionTaken = false;
            switch (cmd) {
                case "ATTACK","A","ATK","HIT":
                    int dmg = player.getCurrentAttack();
                    monster.setHealth(monster.getHealth() - dmg);
                    System.out.println("Player attacked " + monster.getName() + " for " + dmg + " damage.");
                    playerActionTaken = true;
                    break;

                case "USE":
                    System.out.print("Which item? ");
                    String id = scanner.nextLine().trim().toUpperCase();
                    Item item = player.findInInventory(id);
                    if (item instanceof ConsumableItem) {
                        player.heal(item);
                        System.out.println("You used " + item.getItemName() + ".");
                        playerActionTaken = true;
                    } else {
                        System.out.println("You cannot use that item.");
                    }
                    break;

                case "FLEE","FL":
                    System.out.println("You flee to the previous room before the " + monster.getName() + " stops your escapade.");
                    player.setHealth(50);  // restore 50% HP
                    player.setRoom(previousRoom);
                    return;

                case "HEAL":
                    System.out.print("Which item? ");

                    String healId = scanner.nextLine().trim().toUpperCase();
                    Item healItem = player.findInInventory(healId);
                    if (healItem != null) {
                        player.heal(healItem);
                        playerActionTaken = true;
                    } else {
                        System.out.println("Item not found. Your turn is wasted.");
                        playerActionTaken = true;
                    }
                    break;

                case "INVENTORY":
                    player.showInventory();
                    System.out.print("Do you want to EQUIP, UNEQUIP, or CANCEL? ");

                    String action = scanner.nextLine().trim().toUpperCase();
                    if (action.equals("EQUIP")) {
                        System.out.print("Which item? ");
                        String itemId = scanner.nextLine().trim().toUpperCase();
                        Item it = player.findInInventory(itemId);

                        if (it != null) {
                            player.equip(it);
                        }
                        else { System.out.println("Item not found."); }
                        playerActionTaken = true;
                    }
                    else if (action.equals("UNEQUIP")) {
                        player.unequip();
                        playerActionTaken = true;
                    }
                    else if (action.equals("CANCEL") || action.equals("BACK") || action.equals("EXIT")) {
                        System.out.println("You close your inventory."); playerActionTaken = true;
                    } else {
                        System.out.println("Invalid choice. Your turn is wasted."); playerActionTaken = true;
                    }
                    break;

                default:
                    System.out.println("Invalid action. Choose ATTACK, USE, FLEE, HEAL, or INVENTORY.");
                    break;
            }

            if (monster.getHealth() <= 0) {
                System.out.println("You defeated the " + monster.getName() + "!");
                monster.setDefeated(true);
                return;
            }

            if (playerActionTaken) {
                int monsterDMG = monster.attack();
                System.out.println(monster.getName() + " attacked the player for " + monsterDMG + " damage!");
                player.setHealth(player.getHealth() - monsterDMG);
            }

            if (player.getHealth() <= 0) {
                System.out.println("You flee to the previous room before the "
                        + monster.getName() + " stops your escapade.");
                player.setHealth(50);
                player.setRoom(previousRoom);
                return;
            }
        }
    }
}