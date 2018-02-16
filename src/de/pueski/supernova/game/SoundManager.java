package de.pueski.supernova.game;

/*
 * Copyright (c) 2002-2010 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in enemylaserSource and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of enemylaserSource code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.nio.IntBuffer;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import de.pueski.supernova.SuperNova;

/**
 * <p>
 * Simple sound manager for OpenAL using n sources accessed in a round robin
 * schedule. Source n is reserved for a single buffer and checking for whether
 * it's playing.
 * </p>
 * 
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 1.3 $ $Id: SoundManager.java,v 1.1 2011-11-26 14:47:51
 *          mpue Exp $
 */
public class SoundManager {

	private static final Log log = LogFactory.getLog(SoundManager.class);
	
	private static SoundManager INSTANCE = null;
	
	/** We support at most 256 buffers */
	private int[] buffers = new int[256];

	/** Number of sources is limited tby user (and hardware) */
	private int[] sources;

	/** Our internal scratch buffer */
	private IntBuffer scratchBuffer = BufferUtils.createIntBuffer(256);

	/** Whether we're running in no sound mode */
	private boolean soundOutput;

	/** Current index in our buffers */
	private int bufferIndex;

	/** Current index in our enemylaserSource list */
	private int sourceIndex;

	private boolean soundEnabled = true;

	private static final HashMap<String, Integer> soundBank = new HashMap<String, Integer>();
	
	/**
	 * Creates a new SoundManager
	 */
	private SoundManager() {		
		initialize(256);
		addSound("audio/zap.wav");
		addSound("audio/laser.wav");
		addSound("audio/explosion.wav");
		addSound("audio/explosion2.wav");
		addSound("audio/energy2.wav");
		addSound("audio/energy_low.wav");
		addSound("audio/reload.wav");
		addSound("audio/shield.wav");
		addSound("audio/gameover.wav");
		addSound("audio/laser.wav");
		addSound("audio/hit.wav");
		addSound("audio/click.wav");
		addSound("audio/cinematic_impact.wav");		
		System.out.println("add "+addSound("audio/explosion_short.wav"));
	}
	
	public static SoundManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SoundManager();
		}
		return INSTANCE;
	}

	/**
	 * Plays a sound effect
	 * 
	 * @param buffer
	 *            Buffer index to play gotten from addSound
	 */
	public void playEffect(int buffer) {
		if (SuperNova.getInstance().isSoundEnabled()) {			
			if (soundOutput) {
				// make sure we never choose last channel, since it is used for
				// special sounds
				int channel = sources[(sourceIndex++ % (sources.length - 1))];
				
				// link buffer and enemylaserSource, and play it
				AL10.alSourcei(channel, AL10.AL_BUFFER, buffers[buffer]);
				AL10.alSourcePlay(channel);
			}
		}
	}

	/**
	 * Plays a sound on last enemylaserSource
	 * 
	 * @param buffer
	 *            Buffer index to play gotten from addSound
	 */
	public void playSound(int buffer) {
		if (soundOutput) {
			AL10.alSourcei(sources[sources.length - 1], AL10.AL_BUFFER, buffers[buffer]);
			AL10.alSourcePlay(sources[sources.length - 1]);
		}
	}

	/**
	 * Whether a sound is playing on last enemylaserSource
	 * 
	 * @return true if a enemylaserSource is playing right now on enemylaserSource n
	 */
	public boolean isPlayingSound() {
		return AL10.alGetSourcei(sources[sources.length - 1], AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	/**
	 * Initializes the SoundManager
	 * 
	 * @param channels
	 *            Number of channels to create
	 */
	public void initialize(int channels) {
		try {
			AL.create();

			// allocate sources
			scratchBuffer.limit(channels);
			AL10.alGenSources(scratchBuffer);
			scratchBuffer.rewind();
			scratchBuffer.get(sources = new int[channels]);

			// could we allocate all channels?
			if (AL10.alGetError() != AL10.AL_NO_ERROR) {
				throw new LWJGLException("Unable to allocate " + channels + " sources");
			}

			log.info("Initialized "+channels+" channels.");
			// we have sound
			soundOutput = true;
		}
		catch (LWJGLException le) {
			le.printStackTrace();
			log.info("Sound disabled");
		}
	}

	/**
	 * Adds a sound to the Sound Managers pool
	 * 
	 * @param path
	 *            Path to file to load
	 * @return index into SoundManagers buffer list
	 */
	public int addSound(String path) {
		// Generate 1 buffer entry
		scratchBuffer.rewind().position(0).limit(1);
		AL10.alGenBuffers(scratchBuffer);
		buffers[bufferIndex] = scratchBuffer.get(0);

		// load wave data from buffer
		WaveData wavefile = WaveData.create(path);

		// copy to buffers
		AL10.alBufferData(buffers[bufferIndex], wavefile.format, wavefile.data, wavefile.samplerate);

		// unload file again
		wavefile.dispose();

		log.info("Adding "+path+" with index "+bufferIndex);
		
		soundBank.put(path, bufferIndex);
		
		// return index for this sound
		return bufferIndex++;
		
	}

	/**
	 * Destroy this SoundManager
	 */
	public void destroy() {
		if (soundOutput) {

			// stop playing sounds
			scratchBuffer.position(0).limit(sources.length);
			scratchBuffer.put(sources).flip();
			AL10.alSourceStop(scratchBuffer);

			// destroy sources
			AL10.alDeleteSources(scratchBuffer);

			// destroy buffers
			scratchBuffer.position(0).limit(bufferIndex);
			scratchBuffer.put(buffers, 0, bufferIndex).flip();
			AL10.alDeleteBuffers(scratchBuffer);

			// destory OpenAL
			AL.destroy();
		}
	}
	
	/**
	 * Sets all volumes of all sources to the desired volume 
	 * 
	 * @param volume the volume to set
	 */
	public void adjustAllVolumes(float volume) {
		
		for (int i = 0;i < sources.length;i++) {
			AL10.alSourcef(sources[i], AL10.AL_GAIN, volume);	
		}		
	}
	
	public void adjustVolume(int sourceIndex,float volume) {
		AL10.alSourcef(sources[sourceIndex], AL10.AL_GAIN, volume);
	}

	public int getSound(String key) {
		return soundBank.get(key);
	}

	public boolean isSoundEnabled() {
		System.out.println(soundEnabled);
		return soundEnabled;
	}

	public void setSoundEnabled(boolean soundEnabled) {
		this.soundEnabled = soundEnabled;
	}
	
}
