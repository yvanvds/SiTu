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
		arg singer, vowel;
		^super.new.init(singer, vowel);
	}

	init {
		arg s, v;
		singer = s;
		buffer = ~buffer[singer][v];
		vowel = vowels[s][v];
		scale = buffer.sampleRate / ~s.sampleRate;
		synth = Array.newClear(2);
		isPlaying = false;

		channel = MixerChannel(singer++vowel, ~s, 1, 1, level: 1);
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
		var pitchData, start, end, duration;

		this.prStopCurrentSynth(0.1);
		this.prSwitchCurrentSynth();

		pitchData = vowel[currentPitch.asSymbol];

		start = pitchData.end - 24000;
		end = pitchData.end;
		duration = ((end * scale) - (start * scale)) / buffer.sampleRate / scale;

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

		SystemClock.sched(duration - 0.05, {
			this.endRelease(noteID);
		});
	}

	endRelease {
		arg id;

		if (
			noteID == id and: { isPlaying == true }, // true if still playing the same note
			{
				this.prStopCurrentSynth(0.05);
				isPlaying = false;
			},{}
		);
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
		synth[activeSynth].set(\gate, 0, \decay, decay);
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

		vowels = Dictionary.with(
		*[
			\yvan -> Dictionary.with(
			 *[
				\o -> Dictionary.with(
				*[
					\40->PitchLocation.new(40, 2652, 169937),
					\41->PitchLocation.new(41, 185000, 391088),
					\43->PitchLocation.new(43, 412834, 611659),
					\45->PitchLocation.new(45, 634002, 855565),
					\47->PitchLocation.new(47, 873431, 1078077),
					\48->PitchLocation.new(48, 1095474, 1312788),
					\50->PitchLocation.new(50, 1327834, 1541046),
					\52->PitchLocation.new(52, 1577485, 1747472),
					\53->PitchLocation.new(53, 1776859, 1927222),
					\55->PitchLocation.new(55, 1953199, 2133612),
					\57->PitchLocation.new(57, 2173459, 2390000),
					\59->PitchLocation.new(59, 2427549, 2635906),
					\60->PitchLocation.new(60, 2673183, 2850000),
					\62->PitchLocation.new(62, 2861903, 3053124),
					\64->PitchLocation.new(64, 3077858, 3280000),
					\65->PitchLocation.new(65, 3318433, 3471644),
				]),
				\a -> Dictionary.with(
				*[
					\40->PitchLocation.new(40, 1168, 126512),
					\41->PitchLocation.new(41, 165000, 312511),
					\43->PitchLocation.new(43, 345150, 488548),
					\45->PitchLocation.new(45, 512749, 666000),
					\47->PitchLocation.new(47, 682568, 846000),
					\48->PitchLocation.new(48, 871479, 1024000),
					\50->PitchLocation.new(50, 1045268, 1207700),
					\52->PitchLocation.new(52, 1247724, 1400932),
					\53->PitchLocation.new(53, 1418740, 1586660),
					\55->PitchLocation.new(55, 1610000, 1773528),
					\57->PitchLocation.new(57, 1802000, 1978670),
					\59->PitchLocation.new(59, 2005587, 2165625),
					\60->PitchLocation.new(60, 2195695, 2352000),
					\62->PitchLocation.new(62, 2374000, 2516765),
					\64->PitchLocation.new(64, 2534339, 2676657),
					\65->PitchLocation.new(65, 2708449, 2864000),
				]),
			]),
		]);
	}
}

