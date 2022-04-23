package org.bigorange.game.ashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.bigorange.game.ashley.component.*;
import org.bigorange.game.ashley.system.*;
import org.bigorange.game.map.GameObject;
import org.bigorange.game.utils.Utils;

import static org.bigorange.game.UndergroundQuest.*;

public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();


    private final ImmutableArray<Entity> gameObjEntities;
    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;


    public ECSEngine(final World world, final OrthographicCamera gameCamera) {
        super();
        this.world = world;

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        gameObjEntities = getEntitiesFor(Family.all(GameObjectComponent.class).get());

        addSystem(new PlayerAnimationSystem(gameCamera));
        addSystem(new PlayerCameraSystem(gameCamera));
        addSystem(new PlayerMovementSystem(this, gameCamera));
        addSystem(new BulletMovementSystem(this));
        addSystem(new EnemyAnimationSystem());
        addSystem(new PlayerContactSystem());
        addSystem(new SteeringArriveSystem());
        addSystem(new TargetLostSystem());

        addRenderSystem(new GameRenderSystem(this, this.world, gameCamera));

    }


    // 生成子弹
    public void addBullet(Vector2 start, Vector2 target) {
        final Entity bullet = createEntity();

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


        final BulletComponent bulletCmp = createComponent(BulletComponent.class);
        bulletCmp.startTime = System.currentTimeMillis();
        bulletCmp.maxSpeed = 8;
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
    public void addEnemy(Vector2 spawnLocation, String enemyId) {
        final Entity enemy = createEntity();

        final EnemyComponent enemyCmp = createComponent(EnemyComponent.class);
        enemyCmp.state = EnemyComponent.EnemyState.IDLE;
        enemyCmp.maxSpeed = 2f;
        enemy.add(enemyCmp);

        final Animation4DirectionsComponent ani4dCmp = createComponent(Animation4DirectionsComponent.class);
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas",
                TextureAtlas.class).findRegion(enemyId);
        final TextureRegion[][] textureRegions = atlasRegion.split(32, 32);
        ani4dCmp.aniDown = new Animation<>(0.1f, getKeyFrames(textureRegions[0]), Animation.PlayMode.LOOP);
        ani4dCmp.aniLeft = new Animation<>(0.1f, getKeyFrames(textureRegions[1]), Animation.PlayMode.LOOP);
        ani4dCmp.aniRight = new Animation<>(0.1f, getKeyFrames(textureRegions[2]), Animation.PlayMode.LOOP);
        ani4dCmp.aniUp = new Animation<>(0.1f, getKeyFrames(textureRegions[3]), Animation.PlayMode.LOOP);

        final AnimationComponent aniCmp = createComponent(AnimationComponent.class);
        aniCmp.height = aniCmp.width = 32;

        final SpeedComponent speedCmp = createComponent(SpeedComponent.class);

        final GameObjectComponent gameObjCmp = createComponent(GameObjectComponent.class);
        gameObjCmp.id = MathUtils.random(10000,99999);
        gameObjCmp.type = GameObjectComponent.GameObjectType.ENEMY;


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

    public void addPlayer(Vector2 spawnLocation) {
        final Entity player = createEntity();

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;
        // body
        bodyDef.gravityScale = 0;
        bodyDef.position.set(spawnLocation.x, spawnLocation.y);
        b2dCmp.positionBeforeUpdate.set(bodyDef.position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearVelocity.set(1, 2);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(player);


        // fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);
        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_PLAYER;
        fixtureDef.filter.maskBits = CATEGORY_WORLD | CATEGORY_TILEMAP_OBJECT | CATEGORY_ENEMY | CATEGORY_SENSOR ;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();
        player.add(b2dCmp);

        final PlayerComponent playerCmp = createComponent(PlayerComponent.class);
        final AnimationComponent aniCmp = createComponent(AnimationComponent.class);
        final SpeedComponent speedCmp = createComponent(SpeedComponent.class);
        final SteeringLocationComponent stLocationCmp = createComponent(SteeringLocationComponent.class);
        stLocationCmp.body = b2dCmp.body;

        playerCmp.maxSpeed = 2f;
        player.add(playerCmp);
        player.add(aniCmp);
        player.add(speedCmp);
        player.add(stLocationCmp);


        addEntity(player);
    }

    public void addGameObject(GameObject gameObj, final Animation<Sprite> animation) {
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


        final AnimationComponent aniCmp = createComponent(AnimationComponent.class);
        aniCmp.height = boundaries.height;
        aniCmp.width = boundaries.width;
        aniCmp.animation = animation;
        gameObjEntity.add(aniCmp);

        final GameObjectComponent gameObjCmp = createComponent(GameObjectComponent.class);
        gameObjCmp.id = gameObj.getId();
        gameObjCmp.type = gameObj.getType();
        gameObjEntity.add(gameObjCmp);

        final MapGeneratedComponent mapCmp = createComponent(MapGeneratedComponent.class);
        gameObjEntity.add(mapCmp);

        addEntity(gameObjEntity);
    }

    public ImmutableArray<Entity> getGameObjEntities() {
        return gameObjEntities;
    }
}
