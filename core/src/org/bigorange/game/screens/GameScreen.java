package org.bigorange.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.TelegramProvider;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import org.bigorange.game.core.ResourceManager;
import org.bigorange.game.core.Utils;
import org.bigorange.game.core.assets.MapAsset;
import org.bigorange.game.core.assets.MusicAsset;
import org.bigorange.game.core.dialogue.Choice;
import org.bigorange.game.core.dialogue.DialogueNode;
import org.bigorange.game.core.message.MessageType;
import org.bigorange.game.core.screens.BaseScreen;
import org.bigorange.game.ecs.ECSEngine;
import org.bigorange.game.ecs.system.PlayerAnimationSystem;
import org.bigorange.game.ecs.system.PlayerContactSystem;
import org.bigorange.game.ecs.system.PlayerControlSystem;
import org.bigorange.game.map.Map;
import org.bigorange.game.map.MapManager;
import org.bigorange.game.ui.DialogueBox;
import org.bigorange.game.ui.TTFSkin;

import java.util.List;

public class GameScreen extends BaseScreen implements PlayerContactSystem.PlayerContactListener, TelegramProvider,Telegraph {
    private final ECSEngine ecsEngine;
    private final World world;

    private DialogueBox infoBox;
    private DialogueBox infoBox2;
    private ProgressBar progressBar;
    private I18NBundle i18NBundle;
    private Table table;

    public GameScreen(TTFSkin skin) {
        super(skin);

        final MapManager mapManager = Utils.getMapManager();
        final ResourceManager resourceManager = Utils.getResourceManager();

        // TODO init box2d
        Box2D.init();
        world = new World(new Vector2(0,0), true);
        world.setContactListener(Utils.getWorldContactManager());
        world.setContinuousPhysics(true);

        // TODO, init Box2D light system

        ecsEngine = new ECSEngine(world, new OrthographicCamera());
        ecsEngine.getSystem(PlayerContactSystem.class).addPlayerContactListener(this);

        final TiledMap tiledMap = resourceManager.get(MapAsset.LEVEL1.getFilePath(), TiledMap.class);
        mapManager.loadMap(tiledMap, world);
        mapManager.spawnGameObjects(this.ecsEngine, this.ecsEngine.getGameObjEntities());

        final Map currentMap = mapManager.getCurrentMap();
        final Array<Vector2> playerStartLocations = currentMap.getPlayerStartLocations();
        this.ecsEngine.addPlayer(playerStartLocations.get(0));
        //setCursor();
        addEnemies();
        addNpcs();
        addUI();
    }

    @Override
    public void show() {
        super.show();


        Utils.getInputManager().addKeyInputListener(ecsEngine.getSystem(PlayerControlSystem.class));
        Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerControlSystem.class));
        Utils.getInputManager().addMouseInputListener(ecsEngine.getSystem(PlayerAnimationSystem.class));
        Utils.getAudioManager().playMusic(MusicAsset.TALKING);
    }

    @Override
    public void render(float delta) {
        ecsEngine.update(delta);
        world.step(delta, 6, 2);
        stage.act(delta);

        ecsEngine.render(delta);
        stage.getViewport().apply();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        ecsEngine.resize(width, height);
        super.resize(width, height);
    }

    @Override
    public void hide() {
        super.hide();

    }

    @Override
    public void wallContact() {

    }

    public void addEnemies(){
        MapManager mapManager = Utils.getMapManager();
        Map currentMap = mapManager.getCurrentMap();

        for (Vector2 location : currentMap.getEnemyStartLocations()) {
            ecsEngine.addEnemy(location, "Dog 01-3");
        }
    }

    public void addNpcs(){
        MapManager mapManager = Utils.getMapManager();
        Map currentMap = mapManager.getCurrentMap();

        for (Vector2 location : currentMap.getNpcStartLocations()) {
            ecsEngine.addNpc(location, "Dog 01-1");
        }
    }

    private void setCursor(){
        final ResourceManager resourceManager = Utils.getResourceManager();
        final TextureAtlas.AtlasRegion atlasRegion = Utils.getResourceManager().get("characters/characters.atlas",
                TextureAtlas.class).findRegion("crosshair");

        final TextureData textureData = atlasRegion.getTexture().getTextureData();
        if(!textureData.isPrepared()){
            textureData.prepare();
        }
        Pixmap pixmap = new Pixmap(atlasRegion.getRegionWidth(), atlasRegion.getRegionHeight(), textureData.getFormat());
        pixmap.drawPixmap(
                textureData.consumePixmap(),
                0,
                0,
                atlasRegion.getRegionX(),
                atlasRegion.getRegionY(),
                atlasRegion.getRegionWidth(),
                atlasRegion.getRegionHeight()
        );
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 0,0));
        pixmap.dispose();
    }

    @Override
    public Object provideMessageInfo(int msg, Telegraph receiver) {
        return this;
    }

    private void addUI(){
        MessageManager.getInstance().addProvider(this, MessageType.MSG_PLAYER_TALK_TO_NPC);
        progressBar = new ProgressBar(0, 100, 1f, false, skin, "default");
        progressBar.setValue(50f);
        infoBox = new DialogueBox("", skin,"info_frame");
        infoBox2 = new DialogueBox("", skin, "info_frame");

        infoBox.setVisible(false);
        infoBox2.setVisible(false);

        infoBox.addListener((Event e) ->{
            if (!(e instanceof InputEvent) ||
                    !((InputEvent)e).getType().equals(InputEvent.Type.touchDown))
                return false;
            if(infoBox.getDialogueNode() != null) {
                MessageManager.getInstance().dispatchMessage(0.2f, MessageType.MSG_PLAYER_TALK_TO_NPC, infoBox.getDialogueNode());
            }
            return false;
        });

        infoBox2.addListener((Event e) ->{
            if (!(e instanceof InputEvent) ||
                    !((InputEvent)e).getType().equals(InputEvent.Type.touchDown))
                return false;
            if(infoBox2.getDialogueNode() != null) {
                MessageManager.getInstance().dispatchMessage(0.2f, MessageType.MSG_PLAYER_TALK_TO_NPC, infoBox2.getDialogueNode());
            }
            return false;
        });


        table = new Table();
        table.setFillParent(true);
        table.add(progressBar).left().pad(5,5,5,5).row();
        Table infoBoxTable = new Table();
        infoBoxTable.add(infoBox2).expandX().row();
        infoBoxTable.add(infoBox).expandX();
        table.add(infoBoxTable).expand().bottom();

        // Add Message listener
        MessageManager.getInstance().addListener(this, MessageType.MSG_NPC_TALK_TO_PLAYER);
        MessageManager.getInstance().addListener(this, MessageType.MSG_PLAYER_LEAVE_NPC);

        final ResourceManager resourceManager = Utils.getResourceManager();
        i18NBundle = resourceManager.get("i18n/strings_zh_CN", I18NBundle.class);
        stage.addActor(table);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message){
            case MessageType.MSG_NPC_TALK_TO_PLAYER ->{
                DialogueNode dialogueNode = (DialogueNode) msg.extraInfo;

                switch (dialogueNode.getNodeType()){
                    case END -> {
                        showInfoMessage("", false);
                    }
                    case MESSAGE -> {
                        showInfoMessage( i18NBundle.format("T"+ dialogueNode.getMessageId()), true);
                    }
                    case CHOICE -> {
                        //dialogueNode.getChoice().get(0)
                        final List<Choice> choiceDialogue = dialogueNode.getChoice();
                        showChoiceMessage(choiceDialogue.get(0), choiceDialogue.get(1), true);
                    }
                }

            }
            case MessageType.MSG_PLAYER_LEAVE_NPC -> {
                showInfoMessage("",false);
            }
        }

        return true;
    }


    public void showInfoMessage(final String message, boolean isVisible) {
        infoBox.setVisible(isVisible);
        infoBox2.setVisible(false);
        infoBox2.setDialogueNode(null);
        infoBox.setDialogueNode(null);

        infoBox.setText(message);
    }


    public void showChoiceMessage(final Choice choice1, final Choice choice2, boolean isVisible) {
        infoBox.setVisible(isVisible);
        infoBox2.setVisible(isVisible);

        infoBox.setDialogueNode(choice1.getNextNode());
        infoBox.setText(i18NBundle.format("T" + choice1.getMessage()));

        infoBox2.setDialogueNode(choice2.getNextNode());
        infoBox2.setText(i18NBundle.format("T" + choice2.getMessage()));

    }
}
