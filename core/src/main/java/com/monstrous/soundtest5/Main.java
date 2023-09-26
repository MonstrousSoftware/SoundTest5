package com.monstrous.soundtest5;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter{
    private Stage stage;
    private Skin skin;
    private Sound sound;
    private Music music;
    private long soundId;
    private static Label keyLabel;
    private static Label mouseLabel;


    static class KeyMonitor extends InputAdapter {
        @Override
        public boolean keyDown(int keycode) {
            keyLabel.setText("keycode: "+keycode+" : "+Input.Keys.toString(keycode));
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            mouseLabel.setText("button down: "+button);
            return super.touchDown(screenX, screenY, pointer, button);
        }
    }

    @Override
    public void create() {
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/Retro Mystic.ogg"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music/music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);

        im.addProcessor(stage);
        im.addProcessor(new KeyMonitor());

        // can we prevent browser from interpreting F1 and F5?
        Gdx.input.setCatchKey(Input.Keys.F1, true);
        Gdx.input.setCatchKey(Input.Keys.F5, true);

        TextButton button = new TextButton("SOUND EFFECT", skin);
        TextButton stop = new TextButton("STOP SOUND EFFECT", skin);
        CheckBox checkBox = new CheckBox("PLAY MUSIC", skin);

        keyLabel = new Label("Press any key for its keycode", skin);
        mouseLabel = new Label("Press mouse button for button code", skin);

        Table screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.add( button );
        screenTable.row();
        screenTable.add( stop );
        screenTable.row();
        screenTable.add( checkBox ).row();
        screenTable.add(keyLabel).row();
        screenTable.add(mouseLabel);
        stage.addActor(screenTable);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                soundId = sound.play();
            }
        });

        stop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                sound.stop(soundId);
            }
        });

        checkBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if(checkBox.isChecked())
                    music.play();
                else
                    music.stop();
            }
        });

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.45f, 0.45f, 0.62f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        sound.dispose();
        music.dispose();
    }
}
