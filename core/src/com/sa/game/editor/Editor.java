package com.sa.game.editor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class Editor {
    public class LayerToRender {
        boolean isRendered = false;
        int layerId = -1;

        public LayerToRender(boolean isRenderer, int layerId) {
            this.isRendered = isRenderer;
            this.layerId = layerId;
        }
    }

    Skin skin;
    Stage stage;
    SpriteBatch batch;

    private ArrayList<LayerToRender> layersToRender = new ArrayList<>(); 

    public Editor() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        layersToRender.add(new LayerToRender(false, 1));
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin(Gdx.files.internal("skins/metal/skin/metal-ui.json"));

        // Create a table that fills the screen. Everything else will go inside this table.
        Window window = new Window("preferences", skin) {

        };
        window.setResizable(true);
        window.setResizeBorder(10);
        Properties properties = new Properties(skin);

        window.add(properties.getTree());

        //table.setFillParent(true);
        stage.addActor(window);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        /*
          button.addListener(new ChangeListener() {
          public void changed (final ChangeEvent event, final Actor actor) {
          System.out.println("Clicked! Is checked: " + button.isChecked());
          button.setText("Good job!");
          }
      
          @Override
          public void changed(final ChangeEvent event, final Actor actor) {
          // TODO Auto-generated method stub
				
          }
          });
        */
        // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
        //table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
        //window.pack();
    }

    public void resize (final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
