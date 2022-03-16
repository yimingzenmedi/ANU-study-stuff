package models.entities;

import models.entities.items.Armor;
import models.entities.items.Weapon;

public class Protagonist {
    private String name;
    private int level;
    private int exp;
    private int baseExp;
    private float expThresholdFactor;
    private int hp;
    private int baseHp;
    private float hpThresholdFactor;
    private int basicAttack;
    private int basicDefence;
    private int attackBoost;
    private int defenceBoost;
    private Integer armor;
    private Integer weapon;
    // calculated:
    private int expThreshold = 10;
    private int maxHp = 10;

    private static final Protagonist instance = new Protagonist();

    private Protagonist() {}

//    private Protagonist(
//            String name,
//            int level,
//            int exp,
//            int baseExp,
//            float expThresholdFactor,
//            int hp,
//            int baseHp,
//            float hpThresholdFactor,
//            int basicAttack,
//            int basicDefence,
//            int attackBoost,
//            int additionalDefence,
//            Integer armor,
//            Integer weapon
//    ) {
//        this.name = name;
//        this.exp = Math.min(baseExp, exp);
//        this.baseExp = baseExp;
//        this.expThresholdFactor = expThresholdFactor;
//        this.hp = Math.min(hp, baseHp);
//        this.baseHp = baseHp;
//        this.hpThresholdFactor = hpThresholdFactor;
//        this.basicAttack = basicAttack;
//        this.basicDefence = basicDefence;
//        this.attackBoost = attackBoost;
//        this.defenceBoost = additionalDefence;
//        this.armor = armor;
//        this.weapon = weapon;
//
////        expThreshold = 10;
////        maxHp = 10;
//        this.setLevel(level);
//    }
//
//    private Protagonist(
//            String name,
//            int exp,
//            int baseExp,
//            float expThresholdFactor,
//            int hp,
//            int baseHp,
//            int basicAttack,
//            int basicDefence,
//            int attackBoost,
//            int additionalDefence,
//            Integer armor,
//            Integer weapon
//    ) {
//        this.name = name;
//        this.level = 1;
//        this.exp = Math.min(baseExp, exp);
//        this.baseExp = baseExp;
//        this.expThresholdFactor = expThresholdFactor;
//        this.hp = Math.min(hp, baseHp);
//        this.baseHp = baseHp;
//        this.basicAttack = basicAttack;
//        this.basicDefence = basicDefence;
//        this.attackBoost = attackBoost;
//        this.defenceBoost = additionalDefence;
//        this.armor = armor;
//        this.weapon = weapon;
//    }

    public static Protagonist getInstance() {
        return instance;
    }

    public static Protagonist getInstance(
            String name,
            int level,
            int exp,
            int baseExp,
            int hp,
            int baseHp,
            int basicAttack,
            int basicDefence,
            int attackBoost,
            int additionalDefence,
            Integer armor,
            Integer weapon
    ) {
        instance.name = name;
        instance.level = level;
        instance.exp = Math.min(exp, baseExp);
        instance.baseExp = baseExp;
        instance.hp = Math.min(hp, baseExp);
        instance.baseHp = baseHp;
        instance.basicDefence = basicDefence;
        instance.basicAttack = basicAttack;
        instance.attackBoost = attackBoost;
        instance.defenceBoost = additionalDefence;
        instance.armor = armor;
        instance.weapon = weapon;
        return instance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(level, 1);
        this.maxHp = (int)(maxHp + level * hpThresholdFactor);
        this.expThreshold = (int)(expThreshold + expThresholdFactor * level);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(Math.min(hp, maxHp), 0);
    }

    public int getAttackBoost() {
        return attackBoost;
    }

    public void setAttackBoost(int attackBoost) {
        this.attackBoost = attackBoost;
    }

    public int getDefenceBoost() {
        return defenceBoost;
    }

    public void setDefenceBoost(int defenceBoost) {
        this.defenceBoost = defenceBoost;
    }

    public int getAttack() {
        return (int) (basicAttack + basicAttack * 0.5 * (level - 1) + attackBoost);
    }

    public int getDefence() {
        return (int) (basicDefence + basicDefence * 0.5 * (level - 1) + defenceBoost);
    }

    public Integer getArmor() {
        return armor;
    }

    public void setArmor(Integer armor) {
        this.armor = armor;
    }

    public void equipArmor(Armor armor) {
        this.armor = armor.getId();
        this.defenceBoost += armor.getDefenceBoost();
    }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon.getId();
        this.attackBoost += weapon.getAttackBoost();
    }

    public Integer getWeapon() {
        return weapon;
    }

    public void setWeapon(Integer weapon) {
        this.weapon = weapon;
    }

    public int getBasicAttack() {
        return basicAttack;
    }

    public void setBasicAttack(int basicAttack) {
        this.basicAttack = basicAttack;
    }

    public int getBasicDefence() {
        return basicDefence;
    }

    public void setBasicDefence(int basicDefence) {
        this.basicDefence = basicDefence;
    }

    public int getBaseExp() {
        return baseExp;
    }

    public void setBaseExp(int baseExp) {
        this.baseExp = baseExp;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = Math.min(baseExp, exp);
    }

    public float getExpThresholdFactor() {
        return expThresholdFactor;
    }

    public void setExpThresholdFactor(float expThresholdFactor) {
        this.expThresholdFactor = expThresholdFactor;
    }

    public void gainExp(int exp) {
        int newExp = this.exp + exp;
        if (newExp >= this.expThreshold) {
            newExp -= this.expThreshold;
            int newLevel = this.level + 1;
            this.setHp(this.hp + 1);
            this.setLevel(newLevel);
            this.exp = 0;
            gainExp(newExp);
        } else {
            this.setExp(newExp);
        }
    }

    public void recoverHp(int hpRecover) {
        this.setHp(hp + hpRecover);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHpThresholdFactor() {
        return hpThresholdFactor;
    }

    public int getExpThreshold() {
        return expThreshold;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    @Override
    public String toString() {
        return "Protagonist{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", exp=" + exp +
                ", baseExp=" + baseExp +
                ", expThresholdFactor=" + expThresholdFactor +
                ", hp=" + hp +
                ", baseHp=" + baseHp +
                ", hpThresholdFactor=" + hpThresholdFactor +
                ", basicAttack=" + basicAttack +
                ", basicDefence=" + basicDefence +
                ", attackBoost=" + attackBoost +
                ", defenceBoost=" + defenceBoost +
                ", armor=" + armor +
                ", weapon=" + weapon +
                ", expThreshold=" + expThreshold +
                ", maxHp=" + maxHp +
                '}';
    }
}
