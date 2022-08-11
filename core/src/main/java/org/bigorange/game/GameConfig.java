package org.bigorange.game;

/**
 * 游戏参数配置
 */
public class GameConfig {
    /**
     * 屏幕尺寸与显示尺寸的比例
     */
    public static final float UNIT_SCALE = 1 / 64f;

    // Box2D 配置项
    // ==========================================

    //玩家
    public static final short CATEGORY_PLAYER = 1<<0;
    //敌人
    public static final short CATEGORY_ENEMY = 1<<1;
    //NPC
    public static final short CATEGORY_NPC = 1<<2;
    //玩家的子弹
    public static final short CATEGORY_PLAYER_BULLET = 1<<3;
    //敌人的子弹
    public static final short CATEGORY_ENEMY_BULLET = 1<<4;

    //TileMap生成的对象
    public static final short CATEGORY_TILEMAP_OBJECT = 1<<5;

    //传感器
    public static final short CATEGORY_SENSOR = 1<<9;

    //碰撞区域, 碰撞多边形, 世界
    public static final short CATEGORY_WORLD = 1<<10;

    // TODO, 需要Entity生成器，来判断碰撞
    // 不能和Player碰撞
    public static final short MASK_PLAYER = ~CATEGORY_PLAYER;

    //不能和TILEMAP OBJECT碰撞
    public static final short MASK_TILEMAP_OBJECT = ~CATEGORY_TILEMAP_OBJECT;

    //不能和ENEMY 碰撞
    public static final short MASK_ENEMY = ~CATEGORY_ENEMY;

    //只能和PLAYER碰撞
    public static final short MASK_SENSOR = CATEGORY_PLAYER;

    public static final short MASK_BULLET = ~CATEGORY_PLAYER_BULLET;
    //可以和任何物体碰撞
    public static final short MASK_GROUND = -1;
}
