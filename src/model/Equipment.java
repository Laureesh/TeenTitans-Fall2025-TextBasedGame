package model;

import java.util.HashMap;
import java.util.Map;

// Author: Laureesh Volmar

public class Equipment {

    // Equipped weapon (only one)
    private WeaponItem equippedWeapon;

    // Armor slots: head, torso, neck
    private final Map<String, ArmorItem> equippedArmor = new HashMap<>();

    //   WEAPON HANDLING
    public boolean equipWeapon(WeaponItem weapon) {
        if (weapon == null) return false;

        this.equippedWeapon = weapon;
        return true;
    }

    public WeaponItem getEquippedWeapon() {
        return equippedWeapon;
    }

    public int getWeaponAttackBonus() {
        return (equippedWeapon != null) ? equippedWeapon.getAttackBonus() : 0;
    }

    //   ARMOR HANDLING
    public boolean equipArmor(ArmorItem armor) {
        if (armor == null) return false;
        String slot = armor.getSlot();

        if (slot == null || slot.isEmpty()) {
            System.out.println("Armor has no valid slot assigned.");
            return false;
        }

        equippedArmor.put(slot, armor);
        return true;
    }

    public ArmorItem getArmorPiece(String slot) {
        return equippedArmor.get(slot);
    }

    public int getTotalDefenseBonus() {
        int total = 0;
        for (ArmorItem piece : equippedArmor.values()) {
            total += piece.getDefenseBonus();
        }
        return total;
    }

    //   CLEAN UP
    public void unequipWeapon() {
        this.equippedWeapon = null;
    }

    public void unequipArmor(String slot) {
        if (slot != null) {
            equippedArmor.remove(slot);
        }
    }
}