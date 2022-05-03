package org.bigorange.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class CharacterComponent implements Component, Pool.Poolable {
    public float health;
    public float magic;
    public float maxHealth;
    public float maxMagic;
    public long birthTime; // 生成时间


    @Override
    public void reset() {
        health = 0;
        magic = 0;
        maxHealth = 0;
        maxMagic = 0;
        birthTime = 0;
    }
}
