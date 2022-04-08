package org.bigorange.game.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import org.bigorange.game.ecs.component.*;
import org.bigorange.game.ecs.system.*;
import org.bigorange.game.map.MapGameObject;

import static org.bigorange.game.UndergroundQuest.*;

public class ECSEngine extends EntityEngine {
    private static final String TAG = ECSEngine.class.getSimpleName();

    public static final ComponentMapper<PlayerComponent> playerCmpMapper =
            ComponentMapper.getFor(PlayerComponent.class);

    public static final ComponentMapper<BulletComponent> bulletCmpMapper =
            ComponentMapper.getFor(BulletComponent.class);


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

        addSystem(new PlayerAnimationSystem());
        addSystem(new PlayerCameraSystem(gameCamera));
        addSystem(new PlayerMovementSystem(this, gameCamera));
        addSystem(new BulletMovementSystem(this));
        addRenderSystem(new GameRenderSystem(this, this.world, gameCamera));

    }

    // 生成子弹
    public void addBullet(Vector2 start, Vector2 target) {
        final Entity bullet = createEntity();

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;

        // body
        bodyDef.gravityScale = 1;
        bodyDef.position.set(start);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.linearVelocity.set(2,3);
        b2dCmp.positionBeforeUpdate.set(bodyDef.position);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(bullet);

        // fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);
        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_WORLD | BIT_GAME_OBJECT;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();
        bullet.add(b2dCmp);


        final BulletComponent bulletCmp = createComponent(BulletComponent.class);
        bulletCmp.startTime = System.currentTimeMillis();
        bulletCmp.maxSpeed = 4;

        bullet.add(bulletCmp);

        addEntity(bullet);

    }

    public void addPlayer(Vector2 spawnLocation) {
        final Entity player = createEntity();

        final Box2DComponent b2dCmp = createComponent(Box2DComponent.class);
        b2dCmp.height = 0.2f;
        b2dCmp.width = 0.2f;
        // body
        bodyDef.gravityScale = 1;
        bodyDef.position.set(spawnLocation.x * UNIT_SCALE, spawnLocation.y * UNIT_SCALE);
        b2dCmp.positionBeforeUpdate.set(bodyDef.position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearVelocity.set(1,2);
        b2dCmp.body = world.createBody(bodyDef);
        b2dCmp.body.setUserData(player);


        // fixture
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(b2dCmp.width * 0.5f, b2dCmp.height * 0.5f);
        fixtureDef.isSensor = false;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_WORLD | BIT_GAME_OBJECT;

        b2dCmp.body.createFixture(fixtureDef);
        shape.dispose();
        player.add(b2dCmp);


        final PlayerComponent playerCmp = createComponent(PlayerComponent.class);
        final AnimationComponent aniCmp = createComponent(AnimationComponent.class);


        playerCmp.maxSpeed = 2f;
        player.add(playerCmp);
        player.add(aniCmp);

        addEntity(player);
    }

    public void addGameObject(MapGameObject gameObj, final Animation<Sprite> animation) {
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
        fixtureDef.filter.categoryBits = BIT_GAME_OBJECT;
        fixtureDef.filter.maskBits = BIT_PLAYER;
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


        addEntity(gameObjEntity);
    }

    public ImmutableArray<Entity> getGameObjEntities() {
        return gameObjEntities;
    }
}
