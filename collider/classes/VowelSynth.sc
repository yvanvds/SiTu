VowelSynth {
	classvar vowels;
	classvar blackKeys;

	var singer, vowel;

	var currentPitch;
	var currentSpeedAdjust;
	var currentAmp;

	var buffer;
	var scale;
	var synth;

	var channel;

	var activeSynth = 0;
	var <isPlaying;

	var noteID = 0; // used to keep track of which note is playing. Loop is only triggered when the note is still the same.

	*new {
		arg singer, vowel, out, pan;
		^super.new.init(singer, vowel, out, pan);
	}

	init {
		arg s, v, o, p;
		singer = s;
		buffer = ~buffer[singer][v];
		vowel = ~singers[s][v];
		scale = buffer.sampleRate / ~s.sampleRate;
		synth = Array.newClear(2);
		isPlaying = false;

		channel = MixerChannel(singer++vowel, ~s, 1, 2, level: 1, pan: p, outbus: o);
	}

	play {
		arg pitch, velocity;

		if (
			isPlaying,
			{ this.continue(pitch, velocity); },
			{ this.start(pitch, velocity); },
		);
	}

	start {
		arg pitch, velocity;
		var pitchData, start, end, duration, pitchResult;

		this.prStopCurrentSynth(0.01);
		this.prSwitchCurrentSynth();

		noteID = noteID + 1;
		this.prCalculatePitch(pitch);
		pitchData = vowel[currentPitch.asSymbol];

		// ("starting pitch "++pitch++" on synth "++activeSynth).postln;

		start = pitchData.start;
		end = pitchData.end - rrand(48000, 72000);
		duration = ((end * scale) - (start * scale)) / buffer.sampleRate / scale;
		duration = duration * currentSpeedAdjust;
		currentAmp = velocity.linexp(1, 127, 0.01, 0.3);

		isPlaying = true;

		synth[activeSynth] = channel.play(
			\vowel,
			[
				\buf, buffer.bufnum,
				\amp, currentAmp,
				\start, start,
				\speed, currentSpeedAdjust * scale,
				\gate, 1,
			]
		);

		SystemClock.sched(duration - 0.1, {
			this.loop(noteID);
		});
	}

	continue {
		arg pitch, velocity;
		var pitchData, start, end, duration;

		this.prStopCurrentSynth(0.1);
		this.prSwitchCurrentSynth();

		noteID = noteID + 1;
		this.prCalculatePitch(pitch);
		pitchData = vowel[currentPitch.asSymbol];

		// ("continue pitch "++pitch++" on synth "++activeSynth).postln;

		start = pitchData.start + rrand(24000, 48000);
		end = pitchData.end - rrand(48000, 72000);
		duration = ((end * scale) - (start * scale)) / buffer.sampleRate / scale;
		duration = duration * currentSpeedAdjust;
		currentAmp = velocity.linexp(1, 127, 0.01, 0.3);

		isPlaying = true;

		synth[activeSynth] = channel.play(
			\vowel,
			[
				\buf, buffer.bufnum,
				\amp, currentAmp,
				\start, start,
				\speed, currentSpeedAdjust * scale,
				\attack, 0.1,
				\gate, 1,
			]
		);

		SystemClock.sched(duration - 0.1, {
			this.loop(noteID);
		});
	}

	loop {
		arg id;

		if (
			noteID == id and: { isPlaying == true }, // true if still playing the same note
			{
				var pitchData, start, end, duration;

				this.prStopCurrentSynth(0.1);
				this.prSwitchCurrentSynth();

				pitchData = vowel[currentPitch.asSymbol];
				start = pitchData.start + rrand(36000, 60000);
				end = pitchData.end - rrand(48000, 72000);
				duration = ((end * scale) - (start * scale)) / buffer.sampleRate / scale;
				duration = duration * currentSpeedAdjust;

				// ("looping pitch "++pitchData.pitch++" on synth "++activeSynth).postln;

				synth[activeSynth] = channel.play(
					\vowel,
					[
						\buf, buffer.bufnum,
						\amp, currentAmp,
						\start, start,
						\speed, currentSpeedAdjust * scale,
						\attack, 0.1,
						\gate, 1,
					]
				);

				SystemClock.sched(duration - 0.1, {
					this.loop(id);
				});
			},
			{}
		);
	}

	stop {
		this.prStopCurrentSynth(0.05);
		isPlaying = false;
	}



	release {
		var pitchData, start, end, duration, releaseSynth;

		this.prStopCurrentSynth(0.1);
		// this.prSwitchCurrentSynth();

		pitchData = vowel[currentPitch.asSymbol];

		start = pitchData.end - 24000;
		end = pitchData.end;
		duration = ((end * scale) - (start * scale)) / buffer.sampleRate / scale;

		isPlaying = false;

		releaseSynth = channel.play(
			\vowel,
			[
				\buf, buffer.bufnum,
				\amp, currentAmp,
				\start, start,
				\speed, currentSpeedAdjust * scale,
				\attack, 0.1,
				\gate, 1,
			]
		);

		SystemClock.sched(duration - 0.05, {
			releaseSynth.set(\gate, 0, \decay, 0.05);
		});
	}

	setAmplitude {
		arg target;

		channel.level = target;
	}

	setTargetAmplitude {
		arg target, time;

		channel.levelTo(target, time, DbFaderWarp.asWarp);
	}


	prStopCurrentSynth {
		arg decay = 0.05;

		if (
			isPlaying,
			{
				synth[activeSynth].set(\gate, 0, \decay, decay);
				// ("stopping "++activeSynth).postln;
			},{}
		);
	}

	prSwitchCurrentSynth {
		if (
			activeSynth == 0,
			{ activeSynth = 1; },
			{ activeSynth = 0; },
		);
	}

	prCalculatePitch {
		arg pitch;
		var floor, modulus, remainder, speed;

		floor = pitch.floor;
		modulus = floor % 12;
		remainder = pitch % 1;

		if (
			remainder > 0.5,
			{
				floor = floor + 1;
				remainder = -1 + remainder;
			},{}
		);


		if (
			blackKeys.includes(modulus.asInteger),
			{
				floor = floor + 1;
				remainder = remainder - 1;
			}, {}
		);

		speed = 2.pow(remainder.neg / 12.0);

		currentPitch = floor.asInteger;
		currentSpeedAdjust = speed;
	}

	*initClass {
		blackKeys = Array[1, 3, 6, 8, 10];
	}
}

