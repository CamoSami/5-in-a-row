package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	public static int MENU = 0;
	public static int CASUAL = 1;
	public static int COMPETITIVE = 2;
	
	public static int PLAYER_MOVE = 0;
	public static int ENEMY_MOVE  = 1;
	public static int LOST = 2;
	public static int WIN = 3;
	public static int TIME_TICK = 4;
	
	private Clip[] songs, sfx;
	private int currentSongID;
	
	private float songVolume = 0.3f;
	private float sfxVolume = 1f;
	
	private boolean songMute;
	private boolean sfxMute;
	
//	private Random rnd = new Random();
	
	public AudioPlayer() {
		loadSongs();
		loadSfx();
		
		playSong(MENU);
	}
	
	private void loadSongs() {
		// TODO: add missing songs
		String[] names = {"menu"};
		songs = new Clip[names.length];
		
		for (int i = 0; i < songs.length; i++) {
			songs[i] = getClip(names[i]);
		}
		
		updateSongVolume();
	}
	
	private void loadSfx() {
		// TODO: add missing sounds
		String[] names = {"nyehehehe", "trongTH"};
		sfx = new Clip[names.length];
		
		for (int i = 0; i < sfx.length; i++) {
			sfx[i] = getClip(names[i]);
		}
		
		updateSfxVolume();
	}
	
	private void setSongVolume(float volume) {
		this.songVolume = volume;
		updateSongVolume();
	}
	
	private void setSfxVolume(float volume) {
		this.sfxVolume = volume;
		updateSfxVolume();
	}
	
	private void stopSong() {
		if (songs[currentSongID].isActive()) {
			songs[currentSongID].stop();
		}
	}
	
	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;
		
		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			
			return c;
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {	
			e.printStackTrace();
			
		}
		
		return null;
	}
	
	public void toggleSongMute() {
		this.songMute = !songMute;
		
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}
	
	public void toggleSfxMute() {
		this.sfxMute = !sfxMute;
		
		for (Clip c : sfx) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(sfxMute);
		}
		
		if (!sfxMute) {
			playSfx(ENEMY_MOVE);
		}
	}
	
	public void playSong(int songIndex) {
		stopSong();
		
		currentSongID = songIndex;
		updateSongVolume();
		
		songs[currentSongID].setMicrosecondPosition(0);
		songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
//	public void playRndSfx() {
//		int start = 4;
//		start += rnd.nextInt(3);
//		playSfx(start);
//	}
	
	public void playSfx(int sfxIndex) {
		sfx[sfxIndex].setMicrosecondPosition(0);
		
		sfx[sfxIndex].start();
	}

	private void updateSongVolume() {
		FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * songVolume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}
	
	private void updateSfxVolume() {
		for (Clip c : sfx) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * sfxVolume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
 }
