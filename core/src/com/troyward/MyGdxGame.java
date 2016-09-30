package com.troyward;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion down,up,right,left,stand;
	float x,y,xv,yv,totalTime;
	boolean faceRight = true;
	Animation walkRight;
	Animation walkUp;
	Animation walkDown;

	static final float VELOCITY = 200;
	static final float SPACE_VELOCITY = 400;
	static final float FRICTION = 0.9f;
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int DRAW_WIDTH = WIDTH * 3;
	static final int DRAW_HEIGHT = HEIGHT * 3;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(img,WIDTH,HEIGHT);
		down = grid[6][0];
		up = grid[6][1];
		right = grid[6][3];
		stand = grid[6][2];
		left = new TextureRegion(right);
		left.flip(true,false);
		walkRight = new Animation(0.3f,grid[6][2],grid[6][3]);
	}

	@Override
	public void render () {
		totalTime += Gdx.graphics.getDeltaTime();
		move();

		TextureRegion person;
		if (yv > 0) {
			person = up;
		}
		else if (yv < 0) {
			person = down;
		}
		else if (xv > 0) {
			person = walkRight.getKeyFrame(totalTime);
		}
		else if (xv < 0) {
			person = left;
		} else {
			person = stand;
		}


		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(person, x, y, DRAW_WIDTH, DRAW_HEIGHT);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void move () {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			yv = VELOCITY;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			yv = VELOCITY * -1;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			xv = VELOCITY;
			faceRight = true;
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			xv = VELOCITY * -1;
			faceRight = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			yv = SPACE_VELOCITY;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			yv = SPACE_VELOCITY * -1;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			xv = SPACE_VELOCITY;
			faceRight = true;
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			xv = SPACE_VELOCITY * -1;
			faceRight = false;
		}

		x += xv * Gdx.graphics.getDeltaTime();
		y += yv * Gdx.graphics.getDeltaTime();

		xv = decelerate(xv);
		yv = decelerate(yv);
	}

	public float decelerate(float velocity) {
		velocity *= FRICTION;
		if (Math.abs(velocity) < 75) {
			velocity = 0;
		}
		return velocity;
	}
}
