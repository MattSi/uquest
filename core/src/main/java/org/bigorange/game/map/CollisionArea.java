package org.bigorange.game.map;

import com.badlogic.gdx.math.Vector2;

import static org.bigorange.game.GameConfig.UNIT_SCALE;


public class CollisionArea {
    private final Vector2 startLocation;
    private final float[] vertices;


    public CollisionArea(final float x, final float y, final float[] vertices) {
        this.startLocation = new Vector2(x * UNIT_SCALE, y * UNIT_SCALE);
        this.vertices = vertices;
        for (int i = 0; i < vertices.length; i += 2) {
            this.vertices[i] *= UNIT_SCALE;
            this.vertices[i + 1] *= UNIT_SCALE;
        }
    }

    public Vector2 getStartLocation() {
        return startLocation;
    }

    public float[] getVertices() {
        return vertices;
    }
}
