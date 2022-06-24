package org.bigorange.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class StatusUI extends Window implements StatusSubject {
    private Image hpBar;
    private Image mpBar;
    private Image xpBar;

    private ImageButton inventoryButton;
    private ImageButton questButton;
    private Array<StatusObserver> observers;


    private int levelVal = -1;
    private int goldVal = -1;
    private int hpVal = -1;
    private int mpVal = -1;
    private int xpVal = -1;

    private int xpCurrentMax = -1;
    private int hpCurrentMax = -1;
    private int mpCurrentMax = -1;

    private Label hpValLabel;
    private Label mpValLabel;
    private Label xpValLabel;
    private Label levelLabel;
    private Label goldValLabel;

    private float barWidth = 0;
    private float barHeight = 0;

    public StatusUI(Skin skin, TextureAtlas atlas) {
        super("stats", skin);

        observers = new Array<>();

        // groups
        WidgetGroup group = new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        //images
        hpBar = new Image(atlas.findRegion("HP_Bar"));
        Image bar = new Image(atlas.findRegion("Bar"));
        mpBar = new Image(atlas.findRegion("MP_Bar"));
        Image bar2 = new Image(atlas.findRegion("Bar"));
        xpBar = new Image(atlas.findRegion("XP_Bar"));
        Image bar3 = new Image(atlas.findRegion("Bar"));

        barWidth = hpBar.getWidth();
        barHeight = hpBar.getHeight();

        // labels
        Label hpLabel = new Label("hp: ", skin);
        hpValLabel = new Label(String.valueOf(hpVal), skin);
        Label mpLabel = new Label("mp: ", skin);
        mpValLabel = new Label(String.valueOf(mpVal),skin);
        Label xpLabel =new Label("xp: ", skin);
        xpValLabel = new Label(String.valueOf(xpValLabel), skin);
        Label goldLabel = new Label("gp: ", skin);
        goldValLabel = new Label(String.valueOf(goldVal), skin);

        // TODO add buttons

        // Align images
        hpBar.setPosition(3,6);
        mpBar.setPosition(3,6);
        xpBar.setPosition(3,6);

        //add to widget groups
        group.addActor(bar);
        group.addActor(hpBar);
        group2.addActor(bar2);
        group2.addActor(mpBar);
        group3.addActor(bar3);
        group3.addActor(xpBar);


        // Add to layout
        defaults().expand().fill();

        //account for the title padding
        this.pad(this.getPadTop()+10, 10, 10,10);

        this.add(group).size(bar.getWidth(), bar.getHeight()).padRight(10);
        this.add(hpLabel);
        this.add(hpValLabel).align(Align.left);
        this.row();

        this.add(group2).size(bar2.getWidth(), bar2.getHeight()).padRight(10);
        this.add(mpLabel);
        this.add(mpValLabel).align(Align.left);
        this.row();

        this.add(group3).size(bar3.getWidth(), bar3.getHeight()).padRight(10);
        this.add(xpLabel);
        this.add(xpValLabel).align(Align.left).padRight(20);
        this.row();

        // this.debug();
        this.pack();


    }

    @Override
    public void addObserver(StatusObserver statusObserver) {

    }

    @Override
    public void removeObserver(StatusObserver statusObserver) {

    }

    @Override
    public void removeAllObservers() {

    }

    @Override
    public void notify(int value, StatusObserver.StatusEvent event) {

    }
}
