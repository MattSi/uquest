package org.bigorange.game.gameobjs;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * 游戏对象配置
 * 1) 所有的游戏对象都序列化在JSON文件中，读取到该类中来
 * 2) 该配置应该包含所有的属性
 */
public class GameObjectConfig {
    private String gameObjId;
    private State state = State.IDLE;
    private Direction direction = Direction.DOWN;
    private String conversationConfigPath;
    private String questConfigPath;
    private String currentQuestId;
    private String itemTypeId;
    private String atlas;

    private Array<AnimationConfig> animationConfig;
    private Array<InventotyItem.ItemTypeId> inventory;

    private ObjectMap<String, String> entityProperties;

    GameObjectConfig() {
        animationConfig = new Array<>();
      //  inventory = new Array<>();
        entityProperties = new ObjectMap<>();
    }

    static public class AnimationConfig {
        private float frameDuration = 1.0f;
        private AnimationType animationType;
        private String atlasRegion;
        private int tileWidth;
        private int tileHeight;
        private int stopFrameIndex;

        private Array<GridPoint2> gridPoints;
        private Array<String> subTextures;

        public AnimationConfig() {
            animationType = AnimationType.IDLE;
            gridPoints = new Array<>();
            subTextures = new Array<>();
        }

        public int getStopFrameIndex() {
            return stopFrameIndex;
        }

        public void setStopFrameIndex(int stopFrameIndex) {
            this.stopFrameIndex = stopFrameIndex;
        }

        public float getFrameDuration() {
            return frameDuration;
        }

        public void setFrameDuration(float frameDuration) {
            this.frameDuration = frameDuration;
        }

        public AnimationType getAnimationType() {
            return animationType;
        }

        public void setAnimationType(AnimationType animationType) {
            this.animationType = animationType;
        }

        public String getAtlasRegion() {
            return atlasRegion;
        }

        public void setAtlasRegion(String atlasRegion) {
            this.atlasRegion = atlasRegion;
        }

        public int getTileWidth() {
            return tileWidth;
        }

        public void setTileWidth(int tileWidth) {
            this.tileWidth = tileWidth;
        }

        public int getTileHeight() {
            return tileHeight;
        }

        public void setTileHeight(int tileHeight) {
            this.tileHeight = tileHeight;
        }

        public Array<GridPoint2> getGridPoints() {
            return gridPoints;
        }

        public void setGridPoints(Array<GridPoint2> gridPoints) {
            this.gridPoints = gridPoints;
        }

        public Array<String> getSubTextures() {
            return subTextures;
        }

        public void setSubTextures(Array<String> subTextures) {
            this.subTextures = subTextures;
        }
    }

    public String getGameObjId() {
        return gameObjId;
    }

    public State getState() {
        return state;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getConversationConfigPath() {
        return conversationConfigPath;
    }

    public String getQuestConfigPath() {
        return questConfigPath;
    }

    public String getCurrentQuestId() {
        return currentQuestId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public Array<AnimationConfig> getAnimationConfig() {
        return animationConfig;
    }

    public Array<InventotyItem.ItemTypeId> getInventory() {
        return inventory;
    }

    public ObjectMap<String, String> getEntityProperties() {
        return entityProperties;
    }

    public String getAtlas() {
        return atlas;
    }

    @Override
    public String toString() {
        return "GameObjectConfig{" +
                "gameObjId='" + gameObjId + '\'' +
                ", state=" + state +
                ", direction=" + direction +
                ", conversationConfigPath='" + conversationConfigPath + '\'' +
                ", questConfigPath='" + questConfigPath + '\'' +
                ", currentQuestId='" + currentQuestId + '\'' +
                ", itemTypeId='" + itemTypeId + '\'' +
                ", atlas='" + atlas + '\'' +
                ", animationConfig=" + animationConfig +
                ", inventory=" + inventory +
                ", entityProperties=" + entityProperties +
                '}';
    }
}
