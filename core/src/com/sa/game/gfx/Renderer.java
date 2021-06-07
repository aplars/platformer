package com.sa.game.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Renderer {

    private ArrayList<Sprite> sprites[] = new ArrayList[4];
    private ArrayList<Text> texts = new ArrayList<>();
    private ArrayList<Rectangle> rects = new ArrayList<>();
    private ArrayList<Circle> circles = new ArrayList<>();

    SpriteBatch spriteBatch;
    SpriteBatch textBatch;
    ShapeRenderer shapeRenderer;

    BitmapFont font;
    BitmapFont bigFont;

    	/** Returns a new instance of the default shader used by SpriteBatch for GL2 when no shader is specified. */
	static public ShaderProgram createShader () {
      String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
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
      String fragmentShader = "#ifdef GL_ES\n" //
          + "#define LOWP lowp\n" //
          + "precision mediump float;\n" //
          + "#else\n" //
          + "#define LOWP \n" //
          + "#endif\n" //
          + "uniform int white;\n" //
          + "varying LOWP vec4 v_color;\n" //
          + "varying vec2 v_texCoords;\n" //
          + "uniform sampler2D u_texture;\n" //
          + "void main()\n"//
          + "{\n" //
          + "  vec4 tf = texture2D(u_texture, v_texCoords);\n" //
          + "  vec4 orig = v_color * texture2D(u_texture, v_texCoords);\n" //
          + "  gl_FragColor = float(white)*vec4(tf.w, tf.w, tf.w, tf.w) + float(1-white)*orig;\n" //
          + "}";
      ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
      if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
      shader.pedantic = false;
      return shader;
	}

    ShaderProgram shader;

    public Renderer() {
        spriteBatch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        bigFont = new BitmapFont(Gdx.files.internal("skins/score-font/score-font.fnt"),
                                 Gdx.files.internal("skins/score-font/score-font.png"), false);

        for(int i = 0; i < 4; i++) {
            sprites[i] = new ArrayList<Sprite>();
        }
        shader = createShader();
    }

    public void add(Sprite sprite, int layer) {
        sprites[layer].add(sprite);
    }

    public void add(Text text) {
        texts.add(text);
    }

    public void add(Rectangle rect) {
        rects.add(rect);
    }

    public void add(Circle circle) {
        circles.add(circle);
    }

    public int numberOfSprites(int layer) {
        return sprites[layer].size();
    }

    public void render(OrthographicCamera camera, OrthographicCamera fontCamera) {
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.setShader(shader);
        spriteBatch.begin();
        for (int i = 0; i < 4; ++i) {
            for (Sprite sprite : sprites[i]) {
                if (sprite.white == false) {
                    shader.setUniformi("white", 0);
                    float xPos = (sprite.mirrorX) ? sprite.position.x + sprite.size.x + sprite.offset.x
                            : sprite.position.x + sprite.offset.x;
                    float yPos = sprite.position.y + sprite.offset.y;
                    float xSize = (sprite.mirrorX) ? -sprite.size.x : sprite.size.x;
                    float ySize = sprite.size.y;

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
        spriteBatch.end();
        spriteBatch.begin();
        for (int i = 0; i < 4; ++i) {
            for (Sprite sprite : sprites[i]) {
                if (sprite.white == true) {
                    shader.setUniformi("white", 1);
                    float xPos = (sprite.mirrorX) ? sprite.position.x + sprite.size.x + sprite.offset.x
                        : sprite.position.x + sprite.offset.x;
                    float yPos = sprite.position.y + sprite.offset.y;
                    float xSize = (sprite.mirrorX) ? -sprite.size.x : sprite.size.x;
                    float ySize = sprite.size.y;

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
        spriteBatch.end();

        for (int i = 0; i < 4; ++i) {
            sprites[i].clear();
        }
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeType.Line);
        for (Rectangle rect : rects) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (Circle circle : circles) {
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }

        shapeRenderer.end();
        rects.clear();
        circles.clear();

        float ratew = fontCamera.viewportWidth/camera.viewportWidth;  //<--- you should calculate these 2 only once.
        float rateh = fontCamera.viewportHeight/camera.viewportHeight;

        textBatch.setProjectionMatrix(fontCamera.projection);
        textBatch.setTransformMatrix(fontCamera.view);
        textBatch.begin();
        for(Text text : texts) {
            float x = fontCamera.position.x-(camera.position.x-text.x)*ratew;
            float y = fontCamera.position.y-(camera.position.y-text.y)*rateh;
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
