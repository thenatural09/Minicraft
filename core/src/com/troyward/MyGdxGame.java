package com.troyward;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class MyGdxGame extends ApplicationAdapter implements Disposable {
	SpriteBatch batch;
	Texture img;
	TextureRegion down,up,right,left,stand;
	float x,y,xv,yv,totalTime;
	boolean faceRight = true;
	Animation walkRight;
	Animation walkUp;
	Animation walkDown;
	Music music;

	static final float VELOCITY = 200;
	static final float SPACE_VELOCITY = 400;
	static final float FRICTION = 0.9f;
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final float DRAW_WIDTH = WIDTH * 2.5f;
	static final float DRAW_HEIGHT = HEIGHT * 2.5f;

	@Override
	public void create () {
		//music
		music = Gdx.audio.newMusic(Gdx.files.external("Downloads/FluteyMinicraftSong.mp3"));

		//creating person and animation
		batch = new SpriteBatch();
		img = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(img,WIDTH,HEIGHT);
		down = grid[6][0];
		up = grid[6][1];
		right = grid[6][3];
		stand = grid[6][2];
		left = new TextureRegion(right);
		left.flip(true,false);
		walkRight = new Animation(0.3f,stand,right);
		walkUp = new Animation(0.3f,stand,up);
		walkDown = new Animation(0.3f,stand,down);
	}

	@Override
	public void render () {
		//play music
		music.play();
		//setting total time
		totalTime += Gdx.graphics.getDeltaTime();

		//windowed and person flipping
		move();

		TextureRegion person;
		Gdx.graphics.setWindowedMode(800,600);

		if (x > 725) {
			x = 0;
		}

		if (x < 0) {
			x = 725;
		}

		if (y > 520) {
			y = 0;
		}

		if (y < 0) {
			y = 520;
		}

		if (yv > 0) {
			person = walkUp.getKeyFrame(totalTime,true);
		}
		else if (yv < 0) {
			person = walkDown.getKeyFrame(totalTime,true);
		}
		else if (xv != 0) {
			person = walkRight.getKeyFrame(totalTime,true);
		} else {
			person = stand;
		}

		//color of background
		Gdx.gl.glClearColor(0.2f, 0.85f, 0.2f, 0.75f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//rending person
		batch.begin();
		if (faceRight) {
			batch.draw(person, x, y, DRAW_WIDTH, DRAW_HEIGHT);
		} else {
			batch.draw(person, x + DRAW_WIDTH,y,DRAW_WIDTH*-1,DRAW_HEIGHT);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		music.dispose();
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
