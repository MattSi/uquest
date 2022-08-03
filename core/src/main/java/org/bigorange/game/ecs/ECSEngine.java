package org.bigorange.game.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.bigorange.game.ActionType;
import org.bigorange.game.Utils;
import org.bigorange.game.assets.TextureAtlasAssets;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.ecs.system.*;
import org.bigorange.game.gameobjs.*;
import org.bigorange.game.map.GameObject;
import org.bigorange.game.screens.EScreenType;
import org.bigorange.game.screens.ScreenManager;
import org.bigorange.game.ui.PlayerHUD;

import java.util.EnumMap;

import static org.bigorange.game.GameConfig.*;


public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();


    private final ImmutableArray<Entity> gameObjEntities;
    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;


    public ECSEngine(final World world, final OrthographicCamera gameCamera, final OrthographicCamera hudCamera) {
        super();
        this.world = world;

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        gameObjEntities = getEntitiesFor(Family.all(GameObjectComponent.class).get());
        final ScreenManager screenManager = Utils.getScreenManager();
        final PlayerHUD playerHUD = (PlayerHUD) screenManager.getScreenInstance(EScreenType.PLAYERHUD);
        addSystem(new PlayerCameraSystem(gameCamera));
        addSystem(new PlayerControlSystem(this, gameCamera));
        addSystem(new BulletMovementSystem(this));
        addSystem(new EnemyAnimationSystem());
        addSystem(new PlayerContactSystem());
        addSystem(new SteeringArriveSystem());
        addSystem(new InteractSystem());
        addSystem(new TargetLostSystem());
        addSystem(new AnimationSimpleSystem());
        addSystem(new AnimationSystem(gameCamera));
        addSystem(new RollingSystem());

        addRenderSystem(new GameRenderSystem(this, this.world, gameCamera));

        addRenderSystem(new PlayerHUDRenderSystem(playerHUD));

    }


    // 生成子弹
    public void addBullet(Vector2 start, Vector2 target) {
        final Entity bullet = createEntity();
        final GameObjectConfig bullet1Cfg = GameObjectFactory.getInstance().
                getGameObjectConfig(GameObjectFactory.GameObjectName.BULLET1);

        final GameObjectComponent goCmp = createComponent(GameObjectComponent.class);
        goCmp.birthTime = System.nanoTime();
        goCmp.id = 2222;
        goCmp.type = GameObjectType.MISSILE;
        bullet.add(goCmp);

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;

        // body
        bodyDef.gravityScale = 0;
        bodyDef.position.set(start);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //bodyDef.linearVelocity.set(2,1);
        b2dCmp.positionBeforeUpdate.set(bodyDef.position);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(bullet);

        // fixture
        final CircleShape shape = new CircleShape();
        shape.setRadius(b2dCmp.width * 0.5f);

        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_BULLET;
        fixtureDef.filter.maskBits = MASK_BULLET | CATEGORY_ENEMY | CATEGORY_TILEMAP_OBJECT;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();
        bullet.add(b2dCmp);

        // Add Sprite
        final AnimationSimpleComponent aniCmp = createSimpleAnimationComponent(bullet1Cfg);
        bullet.add(aniCmp);

        final BulletComponent bulletCmp = createComponent(BulletComponent.class);
        bulletCmp.startTime = System.currentTimeMillis();
        bulletCmp.maxSpeed = 8;
        bulletCmp.target = target;

        float tmp = target.x - start.x;
        if(MathUtils.isZero(tmp)){
            if(target.y > start.y){
                bulletCmp.rotation = 0f;
            } else {
                bulletCmp.rotation = 180f;
            }
        } else {
            bulletCmp.rotation = MathUtils.radiansToDegrees* ( MathUtils.PI*3/2 +  MathUtils.atan2((target.y - start.y), (target.x - start.x)));
        }

        bullet.add(bulletCmp);

        float rad = MathUtils.atan2((target.y - start.y), (target.x - start.x));
        final SpeedComponent speedCmp = createComponent(SpeedComponent.class);
        speedCmp.velocity.x = bulletCmp.maxSpeed * MathUtils.cos(rad);
        speedCmp.velocity.y = bulletCmp.maxSpeed * MathUtils.sin(rad);
        bullet.add(speedCmp);

        addEntity(bullet);
    }

    private Array<Sprite> getKeyFrames(final TextureRegion[] textureRegions) {
        final Array<Sprite> keyFrames = new Array<>();

        for (final TextureRegion region : textureRegions) {
            keyFrames.add(new Sprite(region));
        }
        return keyFrames;
    }


    public void addNpc2(Vector2 spawnLocation, String npcId, GameObjectFactory.GameObjectName npcName) {
        GameObjectFactory gof = GameObjectFactory.getInstance();
        GameObjectConfig cfg = gof.getGameObjectConfig(npcName);

        final Entity npc = createEntity();

        // 添加基础Component
        GameObjectComponent2 goCmp2 = createBasicComponent(cfg, spawnLocation);
        npc.add(goCmp2);

        // 添加动画Component
        AnimationComponent aniCmp2 = createAnimationComponent(cfg);
        npc.add(aniCmp2);

        // 添加NPC的行为
        if (!Utils.isStrNullOrEmpty(cfg.getConversationConfigPath())) {
            final ActionableComponent actionCmp = createComponent(ActionableComponent.class);
            actionCmp.type = ActionType.TALK;
            actionCmp.talkScript = cfg.getConversationConfigPath();
            npc.add(actionCmp);
        }

        // 添加速度组件
        final SpeedComponent speedCmp = createComponent(SpeedComponent.class);
        npc.add(speedCmp);

        // 添加标识组件
        final CpuComponent cpuCmp = createComponent(CpuComponent.class);
        cpuCmp.gameObjectType = GameObjectType.NPC;
        npc.add(cpuCmp);

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;

        // body
        bodyDef.gravityScale = 0;
        bodyDef.position.set(spawnLocation);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        bodyDef.angle = 0;
        bodyDef.linearVelocity.set(0, 0);

        //bodyDef.linearVelocity.set(2,1);
        b2dCmp.positionBeforeUpdate.set(spawnLocation);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(npc);

        // fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);

        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = CATEGORY_ENEMY;
        fixtureDef.filter.maskBits = MASK_PLAYER;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();

        // create sensor
        final CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);
        fixtureDef.isSensor = true;
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CATEGORY_SENSOR;
        fixtureDef.filter.maskBits = CATEGORY_PLAYER;

        b2dCmp.body.createFixture(fixtureDef);
        circleShape.dispose();

        npc.add(b2dCmp);


        addEntity(npc);
    }

    public void addEnemy(Vector2 spawnLocation, String enemyId) {
        final Entity enemy = createEntity();

//        final EnemyComponent enemyCmp = createComponent(EnemyComponent.class);
//        enemyCmp.state = EnemyComponent.EnemyState.IDLE;
//        enemyCmp.maxSpeed = 2f;
//        enemy.add(enemyCmp);

        final Animation4DirectionsComponent ani4dCmp = createComponent(Animation4DirectionsComponent.class);
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas",
                TextureAtlas.class).findRegion(enemyId);
        final TextureRegion[][] textureRegions = atlasRegion.split(32, 32);
        ani4dCmp.aniDown = new Animation<>(0.1f, getKeyFrames(textureRegions[0]), Animation.PlayMode.LOOP);
        ani4dCmp.aniLeft = new Animation<>(0.1f, getKeyFrames(textureRegions[1]), Animation.PlayMode.LOOP);
        ani4dCmp.aniRight = new Animation<>(0.1f, getKeyFrames(textureRegions[2]), Animation.PlayMode.LOOP);
        ani4dCmp.aniUp = new Animation<>(0.1f, getKeyFrames(textureRegions[3]), Animation.PlayMode.LOOP);

        final AnimationSimpleComponent aniCmp = createComponent(AnimationSimpleComponent.class);
        aniCmp.height = aniCmp.width = 32;

        final SpeedComponent speedCmp = createComponent(SpeedComponent.class);

        final GameObjectComponent gameObjCmp = createComponent(GameObjectComponent.class);
        gameObjCmp.id = MathUtils.random(10000, 99999);
        gameObjCmp.type = GameObjectType.ENEMY;
        gameObjCmp.state = GameObjectState.IDLED;


        enemy.add(ani4dCmp);
        enemy.add(aniCmp);
        enemy.add(speedCmp);
        enemy.add(gameObjCmp);


        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;

        // body
        bodyDef.gravityScale = 0;
        bodyDef.position.set(spawnLocation);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.angle = 0;
        bodyDef.linearVelocity.set(0, 0);

        //bodyDef.linearVelocity.set(2,1);
        b2dCmp.positionBeforeUpdate.set(spawnLocation);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(enemy);

        // fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);

        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = CATEGORY_ENEMY;
        fixtureDef.filter.maskBits = MASK_PLAYER;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();


        // create sensor
        final CircleShape circleShape = new CircleShape();
        circleShape.setRadius(2f);
        fixtureDef.isSensor = true;
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CATEGORY_SENSOR;
        fixtureDef.filter.maskBits = CATEGORY_PLAYER;

        b2dCmp.body.createFixture(fixtureDef);
        circleShape.dispose();

        enemy.add(b2dCmp);

        final SteeringComponent steeringCmp = createComponent(SteeringComponent.class);
        steeringCmp.body = b2dCmp.body;
        steeringCmp.setMaxLinearSpeed(4);
        steeringCmp.setMaxLinearAcceleration(20);
        steeringCmp.setMaxAngularAcceleration(20);
        enemy.add(steeringCmp);

        addEntity(enemy);

    }

    public void addPlayer2(Vector2 spawnLocation) {
        GameObjectFactory gof = GameObjectFactory.getInstance();
        GameObjectConfig cfg = gof.getGameObjectConfig(GameObjectFactory.GameObjectName.PLAYER);

        final Entity player = createEntity();

        // 添加基础Component
        final GameObjectComponent2 basicCmp = createBasicComponent(cfg, spawnLocation);
        player.add(basicCmp);

        // 添加动画Component
        final AnimationComponent animationCmp = createAnimationComponent(cfg);
        player.add(animationCmp);

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;
        // body
        bodyDef.gravityScale = 0;
        bodyDef.position.set(spawnLocation.x, spawnLocation.y);
        b2dCmp.positionBeforeUpdate.set(bodyDef.position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //bodyDef.linearVelocity.set(1, 2);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(player);


        // fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);
        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_WORLD | CATEGORY_TILEMAP_OBJECT | CATEGORY_ENEMY | CATEGORY_SENSOR;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();
        player.add(b2dCmp);

        final CircleShape circleShape = new CircleShape();
        circleShape.setRadius(5f);
        fixtureDef.isSensor = true;
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = CATEGORY_SENSOR;
        // fixtureDef.filter.maskBits =

        final PlayerComponent playerCmp = createComponent(PlayerComponent.class);

        final SpeedComponent speedCmp = createComponent(SpeedComponent.class);
        speedCmp.maxSpeed = 2f;

        final SteeringLocationComponent stLocationCmp = createComponent(SteeringLocationComponent.class);
        stLocationCmp.body = b2dCmp.body;

        final InteractComponent interactCmp = createComponent(InteractComponent.class);

        player.add(speedCmp);
        player.add(playerCmp);
        player.add(stLocationCmp);
        player.add(interactCmp);

        addEntity(player);
    }

    /**
     * 添加地图对象
     *
     * @param gameObj
     * @param animation
     */
    public void addMapGameObject(GameObject gameObj, final Animation<Sprite> animation) {
        final Entity gameObjEntity = createEntity();
        final Rectangle boundaries = gameObj.getBoundaries();

        final float spawnX = boundaries.x + boundaries.width * 0.5f;
        final float spawnY = boundaries.y + boundaries.height * 0.5f;

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.width = boundaries.width;
        b2dCmp.height = boundaries.height;
        // body
        bodyDef.gravityScale = 0;
        bodyDef.position.set(spawnX, spawnY);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        b2dCmp.positionBeforeUpdate.set(bodyDef.position);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(gameObjEntity);
        // fixtures
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);
        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_TILEMAP_OBJECT;
        fixtureDef.filter.maskBits = MASK_GROUND;
        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();
        gameObjEntity.add(b2dCmp);


        final AnimationSimpleComponent aniCmp = createComponent(AnimationSimpleComponent.class);
        aniCmp.height = boundaries.height;
        aniCmp.width = boundaries.width;
        aniCmp.animation = animation;
        gameObjEntity.add(aniCmp);

        final GameObjectComponent gameObjCmp = createComponent(GameObjectComponent.class);
        gameObjCmp.id = gameObj.getId();
        gameObjCmp.type = gameObj.getType();
        gameObjCmp.state = GameObjectState.IDLED;
        gameObjCmp.isMapGenerated = true;
        gameObjCmp.birthTime = System.nanoTime();
        gameObjEntity.add(gameObjCmp);

        final MapGeneratedComponent mapCmp = createComponent(MapGeneratedComponent.class);
        gameObjEntity.add(mapCmp);


        if (gameObj.isSensor()) {
            // create sensor
            final CircleShape circleShape = new CircleShape();
            circleShape.setRadius(1f);
            fixtureDef.isSensor = true;
            fixtureDef.shape = circleShape;
            fixtureDef.filter.categoryBits = CATEGORY_SENSOR;
            fixtureDef.filter.maskBits = CATEGORY_PLAYER;

            b2dCmp.body.createFixture(fixtureDef);
            circleShape.dispose();

            final ActionableComponent actionCmp = createComponent(ActionableComponent.class);
            actionCmp.type = ActionType.TALK;
            gameObjEntity.add(actionCmp);
        }
//
//        final EnemyComponent enemyCmp = createComponent(EnemyComponent.class);
//        enemyCmp.state = EnemyComponent.EnemyState.IDLE;
//        enemyCmp.maxSpeed = 2f;
//        gameObjEntity.add(enemyCmp);

        addEntity(gameObjEntity);
    }

    public ImmutableArray<Entity> getGameObjEntities() {
        return gameObjEntities;
    }

    /**
     * Create Basic GameObject Component
     *
     * @param cfg
     * @param spawnLocation
     * @return
     */
    private GameObjectComponent2 createBasicComponent(GameObjectConfig cfg, Vector2 spawnLocation) {
        if (cfg == null) {
            throw new GdxRuntimeException("Cannot create Basic Component, because GameObjectConfig is null");
        }
        final GameObjectComponent2 component = createComponent(GameObjectComponent2.class);
        component.gameObjId = cfg.getGameObjId();
        component.isMapGenerated = false;
        component.state = cfg.getState();
        component.birthTime = System.nanoTime();
        component.spawnLocation = spawnLocation;
        component.direction = cfg.getDirection();

        return component;
    }


    /**
     * Create Animation component
     *
     * @param cfg
     * @return
     */
    private AnimationComponent createAnimationComponent(GameObjectConfig cfg) {
        if (cfg == null) {
            throw new GdxRuntimeException("Cannot create Animation2 Component, because GameObjectConfig is null");
        }
        final AnimationComponent component = createComponent(AnimationComponent.class);
        Array<GameObjectConfig.AnimationConfig> animations = cfg.getAnimationConfig();
        if (animations != null && animations.size > 0) {
            component.animations = new EnumMap<AnimationType, AnimationComponent.AnimationPack<Sprite>>
                    (AnimationType.class);
            component.aniType = AnimationType.IDLE;
            component.aniTimer = 0f;
            component.isEnable = true;
            //animations.
            for (GameObjectConfig.AnimationConfig animationCfg : animations) {
                /**
                 * 1. get atlasRegion
                 * 2. if gridPoints is valid, then get gridPoints to divide.
                 * 3. if subTextures is valid, then use subTextures to set animations
                 */
                String atlasRegionName = animationCfg.getAtlasRegion();
                AnimationType animationType = animationCfg.getAnimationType();
                Array<GridPoint2> points = animationCfg.getGridPoints();
                Array<String> subTextures = animationCfg.getSubTextures();
                if (points != null && points.size != 0) {
                    final TextureAtlasAssets textureAtlasAssets = TextureAtlasAssets.valueOf(cfg.getAtlas());
                    TextureAtlas.AtlasRegion region = Utils.getResourceManager().get(textureAtlasAssets.getFilePath(), TextureAtlas.class).
                            findRegion(atlasRegionName);
                    TextureRegion[][] split = region.split(animationCfg.getTileWidth(), animationCfg.getTileHeight());


                    AnimationComponent.AnimationPack<Sprite> aPack = new AnimationComponent.AnimationPack<>();
                    Sprite[] keyFrames = new Sprite[points.size];
                    for (int i = 0; i < points.size; i++) {
                        keyFrames[i] = new Sprite(split[points.get(i).x][points.get(i).y]);
                    }
                    Animation<Sprite> animation = new Animation<>(animationCfg.getFrameDuration(), keyFrames);
                    animation.setPlayMode(Animation.PlayMode.LOOP);
                    aPack.animation = animation;
                    aPack.height = animationCfg.getTileHeight();
                    aPack.width = animationCfg.getTileWidth();
                    component.animations.put(animationType, aPack);
                    component.stopFrameIndex = animationCfg.getStopFrameIndex();
                } else if (subTextures != null && subTextures.size != 0) {
                    final TextureAtlasAssets textureAtlasAssets = TextureAtlasAssets.valueOf(animationCfg.getAtlasRegion());
                    Sprite[] keyFrames = new Sprite[subTextures.size];

                    for (int i = 0; i < subTextures.size; i++) {
                        String subTexture = subTextures.get(i);
                        final TextureAtlas.AtlasRegion region = Utils.getResourceManager().
                                get(textureAtlasAssets.getFilePath(), TextureAtlas.class).findRegion(subTexture);
                        keyFrames[i] = new Sprite(region);
                    }
                    AnimationComponent.AnimationPack<Sprite> aPack = new AnimationComponent.AnimationPack<>();
                    Animation<Sprite> animation = new Animation<Sprite>(animationCfg.getFrameDuration(), keyFrames);
                    animation.setPlayMode(Animation.PlayMode.NORMAL);
                    aPack.animation = animation;
                    aPack.height = animationCfg.getTileHeight();
                    aPack.width = animationCfg.getTileWidth();
                    component.animations.put(animationType, aPack);
                    component.stopFrameIndex = animationCfg.getStopFrameIndex();

                }
            }
        }
        return component;
    }

    /**
     * Create Simple Animation Component
     *
     * @param cfg
     * @return
     */
    private AnimationSimpleComponent createSimpleAnimationComponent(GameObjectConfig cfg) {
        if (cfg == null) {
            throw new GdxRuntimeException("Cannot create Animation Component, because GameObjectConfig is null");
        }

        final AnimationSimpleComponent component = createComponent(AnimationSimpleComponent.class);
        Array<GameObjectConfig.AnimationConfig> animations = cfg.getAnimationConfig();
        // 简单动画,只取数组中第一个元素
        // 简单动画, 默认只有subTexture模式
        if (animations != null && animations.size > 0) {
            //component.animation = new Animation<Sprite>()
            GameObjectConfig.AnimationConfig animationCfg = animations.get(0);
            final Array<String> subTextures = animationCfg.getSubTextures();
            final TextureAtlasAssets textureAtlasAssets = TextureAtlasAssets.valueOf(animationCfg.getAtlasRegion());
            if (subTextures != null && subTextures.size != 0) {
                Sprite[] keyFrames = new Sprite[subTextures.size];

                for(int i=0; i<subTextures.size; i++){
                    String subTexture = subTextures.get(i);
                    final TextureAtlas.AtlasRegion region = Utils.getResourceManager().
                            get(textureAtlasAssets.getFilePath(), TextureAtlas.class).findRegion(subTexture);
                    keyFrames[i] = new Sprite(region);
                }
                component.animation = new Animation<Sprite>(animationCfg.getFrameDuration(), keyFrames);
                component.height = animationCfg.getTileHeight();
                component.width = animationCfg.getTileWidth();
            }

        }


        return component;
    }
}
