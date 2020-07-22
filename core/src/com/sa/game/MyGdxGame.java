package com.sa.game;


import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;

public class MyGdxGame implements ApplicationListener {
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
	OrthogonalTiledMapRenderer mapRenderer;
	StaticEnvironment staticEnvironment;
	Player player;
	ShapeRenderer shapeRenderer;
	float dt = 1/60f;
	Controller controller;

	String testLevel[] = {
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000011111100000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000011111111111000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000111100000000000001",
			"11100000000000000000000000000001",
			"10000000000000000000000000000001",
			"10000000000000000000000000000001",
			"11111111111111111111111111111111"
	};

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		player = new Player(new Vector2(800*0.2f, 0), new Vector2(), new Vector2(32 * 2, 32 * 2));
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		com.badlogic.gdx.utils.Array<Controller> theControllers = Controllers.getControllers();
		for (Controller c:
				theControllers) {
			System.out.println(c.getName());
		}
		controller = theControllers.first();
		controller.addListener(new ControllerAdapter() {
			@Override
			public boolean buttonDown (Controller controller, int buttonIndex) {
				System.out.print("button down ");
				System.out.println(buttonIndex);
				return true;
			}
			@Override
			public boolean povMoved (Controller controller, int povIndex, PovDirection value) {
				System.out.print("pov moved ");
				System.out.print(povIndex);
				System.out.print(" ");
				System.out.println(value);
				return true;
			}
			@Override
			public boolean axisMoved (Controller controller, int axisIndex, float value) {
				System.out.print("axis moved ");
				System.out.print(axisIndex);
				System.out.print(" ");
				System.out.println(value);
				return true;
			}

		});
		staticEnvironment = new StaticEnvironment("statictiles.png", 32, 32, 26);
		staticEnvironment.setLevel(testLevel);
		mapRenderer = new OrthogonalTiledMapRenderer(staticEnvironment.map);
		if(Gdx.graphics.getHeight() > Gdx.graphics.getWidth()) {
			float aspect = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
			//float aspect = (float) staticEnvironment.getMapHeight() / (float) staticEnvironment.getMapWidth();
			camera = new OrthographicCamera(
					staticEnvironment.getMapHeight() * staticEnvironment.tileSizeInPixels,
					aspect * staticEnvironment.getMapHeight() * staticEnvironment.tileSizeInPixels);
			camera.setToOrtho(
					true,
					staticEnvironment.getMapHeight() * staticEnvironment.tileSizeInPixels,
					aspect * staticEnvironment.getMapHeight() * staticEnvironment.tileSizeInPixels);
		}
		else if(Gdx.graphics.getWidth() > Gdx.graphics.getHeight()) {
			float aspect = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();
			//float aspect = (float) staticEnvironment.getMapWidth() / (float) staticEnvironment.getMapHeight();
			camera = new OrthographicCamera(
					aspect * staticEnvironment.getMapWidth() * staticEnvironment.tileSizeInPixels,
					staticEnvironment.getMapWidth() * staticEnvironment.tileSizeInPixels);
			camera.setToOrtho(
					true,
					aspect * staticEnvironment.getMapWidth() * staticEnvironment.tileSizeInPixels,
					staticEnvironment.getMapWidth() * staticEnvironment.tileSizeInPixels);
		}

		//camera.position.x+=staticEnvironment.getMapWidth()*staticEnvironment.tileSizeInPixels/2;
		//camera.position.y+=staticEnvironment.getMapHeight()*staticEnvironment.tileSizeInPixels/2;
		camera.update();
		player.update(dt, new CollissionData(false, new Vector2(0f, 0f)), new CollissionData(false, new Vector2(0f, 0f)));

	}

	@Override
	public void render () {
		player.handleInput(dt, controller);
		player.preUpdate(dt);
		CollissionData groundCollisionData = CollissionDetection.collidesWithGround(player.collissionRectangle, player.velocity, staticEnvironment);
		CollissionData wallsCollisionData = CollissionDetection.colidesWithWalls(player.collissionRectangle, player.velocity, staticEnvironment);
		player.update(dt, groundCollisionData, wallsCollisionData);
		camera.update();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//batch.begin();
		//batch.draw(img, 0, 0);
		//batch.end();
		mapRenderer.setView(camera);
		int []layers = {0, 1};
		mapRenderer.render(layers);

		player.render(dt, camera);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void resize(int width, int height) {
		/*if(height > width) {
			float aspect = (float) height / (float) width;
			camera .viewportWidth = staticEnvironment.getMapHeight() * staticEnvironment.tileSizeInPixels;
			camera.viewportHeight = aspect * staticEnvironment.getMapHeight() * staticEnvironment.tileSizeInPixels;
		}
		else if(width > height) {
			float aspect = (float) width / (float) height;
			camera.viewportWidth = aspect * staticEnvironment.getMapWidth() * staticEnvironment.tileSizeInPixels;
			camera.viewportHeight = staticEnvironment.getMapWidth() * staticEnvironment.tileSizeInPixels;
		}*/
	}


	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
