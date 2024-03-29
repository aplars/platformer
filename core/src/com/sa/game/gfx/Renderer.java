package com.sa.game.gfx;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sa.game.gfx.Sprite.ColorMode;

public class Renderer {
    private static int NUM_LAYERS = 10;
    private final ArrayList<Sprite> sprites[] = new ArrayList[NUM_LAYERS];
    private final ArrayList<Text> texts = new ArrayList<>();
    private final ArrayList<Rectangle> rects = new ArrayList<>();
    private final ArrayList<Circle> circles = new ArrayList<>();

    SpriteBatch spriteBatch;
    SpriteBatch textBatch;
    ShapeRenderer shapeRenderer;

    BitmapFont font;
    BitmapFont bigFont;

    /** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
    static public ShaderProgram createShader () {
        final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "uniform mat4 u_projTrans;\n" //
            + "varying vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "\n" //
            + "void main()\n" //
            + "{\n" //
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "   v_color.a = v_color.a * (255.0/254.0);\n" //
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "}\n";
        final String fragmentShader = "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" //
            + "uniform vec3 u_addativeColor;\n" //
            + "varying LOWP vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "uniform sampler2D u_texture;\n" //
            + "void main()\n"//
            + "{\n" //
            + "  vec4 tf = texture2D(u_texture, v_texCoords);\n" //
            + "  vec4 orig = v_color * texture2D(u_texture, v_texCoords);\n" //
            + "  gl_FragColor = orig+vec4(u_addativeColor, 0.0f);/*float(white)*vec4(tf.w, tf.w, tf.w, tf.w) + float(1-white)*orig;*/\n" //
            + "}";
        final ShaderProgram shaderProg = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProg.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shaderProg.getLog());
        return shaderProg;
    }

    ShaderProgram shader;

    public Renderer() {
        ShaderProgram.pedantic = false;
        spriteBatch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);

        for(int i = 0; i < NUM_LAYERS; i++) {
            sprites[i] = new ArrayList<Sprite>();
        }
        shader = createShader();
    }

    public void add(final Sprite sprite, final int layer) {
        sprites[layer].add(sprite);
    }

    public void add(final Text text) {
        texts.add(text);
    }

    public void add(final Rectangle rect) {
        rects.add(rect);
    }

    public void add(final Circle circle) {
        circles.add(circle);
    }

    public int numberOfSprites(final int layer) {
        return sprites[layer].size();
    }

    public void render(final OrthographicCamera camera, final OrthographicCamera fontCamera) {
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.setShader(shader);
        /*spriteBatch.begin();
        for (int i = 0; i < NUM_LAYERS; ++i) {
            for (final Sprite sprite : sprites[i]) {
                if (sprite.white == false) {
                    shader.setUniformf("u_addativeColor", new Vector3(0, 0, 0));
                    final float xPos = (sprite.mirrorX) ? sprite.position.x + sprite.size.x + sprite.offset.x : sprite.position.x + sprite.offset.x;
                    final float yPos = sprite.position.y + sprite.offset.y;
                    final float xSize = (sprite.mirrorX) ? -sprite.size.x : sprite.size.x;
                    final float ySize = sprite.size.y;

                    spriteBatch.draw(sprite.textureRegion,
                                     xPos,
                                     yPos,
                                     xSize/2f,
                                     ySize/2f,
                                     xSize,
                                     ySize,
                                     1f, 1f,
                                     sprite.rotationDeg); // Draw current frame at (50, 50)
                }
            }
            }
            spriteBatch.end();*/

        for (Sprite.ColorMode colorMode : Sprite.ColorMode.values()) {
            spriteBatch.begin();
            if(colorMode == ColorMode.Normal)
                shader.setUniformf("u_addativeColor", 0, 0, 0);
            if(colorMode == ColorMode.White)
                shader.setUniformf("u_addativeColor", 1, 1, 1);

            for (int i = 0; i < NUM_LAYERS; ++i) {
                for (final Sprite sprite : sprites[i]) {
                    if (colorMode != sprite.colorMode)
                        continue;
                    final float xPos = (sprite.mirrorX) ? sprite.position.x + sprite.size.x + sprite.offset.x
                            : sprite.position.x + sprite.offset.x;
                    final float yPos = sprite.position.y + sprite.offset.y;
                    final float xSize = (sprite.mirrorX) ? -sprite.size.x : sprite.size.x;
                    final float ySize = sprite.size.y;

                    spriteBatch.draw(sprite.textureRegion, xPos, yPos, xSize / 2f, ySize / 2f, xSize, ySize, 1f, 1f,
                            sprite.rotationDeg); // Draw current frame at (50, 50)
                    //}
                }
            }
            spriteBatch.end();
        }
        for (int i = 0; i < NUM_LAYERS; ++i) {
            sprites[i].clear();
        }
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeType.Line);
        for (final Rectangle rect : rects) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (final Circle circle : circles) {
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }

        shapeRenderer.end();
        rects.clear();
        circles.clear();

        final float ratew = fontCamera.viewportWidth/camera.viewportWidth;  //<--- you should calculate these 2 only once.
        final float rateh = fontCamera.viewportHeight/camera.viewportHeight;

        textBatch.setProjectionMatrix(fontCamera.projection);
        textBatch.setTransformMatrix(fontCamera.view);
        textBatch.begin();
        for(final Text text : texts) {
            final float x = fontCamera.position.x-(camera.position.x-text.x)*ratew;
            final float y = fontCamera.position.y-(camera.position.y-text.y)*rateh;
            if (text.font == Text.Font.Medium) {
                font.setColor(text.color);
                font.draw(textBatch, text.chars, x, y);
            }
            if (text.font == Text.Font.Big) {
                bigFont.setColor(text.color);
                bigFont.draw(textBatch, text.chars, x, y);
            }
        }
        texts.clear();
        textBatch.end();
    }
}
