Vocalist {
	var vowels;
	var mix;
	var singer, out;

	var currentVowel, currentPitch, currentVelocity;

	*new {
		arg singer, out;
		^super.new.init(singer, out);
	}

	init {
		arg s, o;

		singer = s;
		out = o;

		vowels = Dictionary.with(
			*[
				\o->VowelSynth(singer, \o, out),
				\a->VowelSynth(singer, \a, out),
			]
		);
		currentVowel = \o;
	}

	play {
		arg pitch, velocity, vowel;

		currentPitch = pitch;
		currentVelocity = velocity;

		if (
			vowel != currentVowel and: vowels[currentVowel].isPlaying,
			{
				// continue with new vowel, manually stop old vowel
				vowels[currentVowel].stop();
				currentVowel = vowel;
				vowels[currentVowel].continue(pitch, velocity);
			},
			{
				// vowel is the same, or old vowel was not playing anyway
				// So just pass to play method of vowel
				currentVowel = vowel;
				vowels[currentVowel].setAmplitude(1);
				vowels[currentVowel].play(pitch, velocity);
			}
		);
	}

	start {
		arg pitch, velocity, vowel;

		currentPitch = pitch;
		currentVelocity = velocity;

		if (
			vowel != currentVowel and: vowels[currentVowel].isPlaying,
			{
				vowels[currentVowel].stop();
			},{}
		);
		currentVowel = vowel;
		vowels[currentVowel].setAmplitude(1);
		vowels[currentVowel].start(pitch, velocity);
	}

	continue {
		arg pitch, velocity, vowel;

		currentPitch = pitch;
		currentVelocity = velocity;

		if (
			vowel != currentVowel and: vowels[currentVowel].isPlaying,
			{
				vowels[currentVowel].stop();
			},{}
		);
		currentVowel = vowel;
		vowels[currentVowel].setAmplitude(1);
		vowels[currentVowel].continue(pitch, velocity);
	}

	stop {
		vowels[currentVowel].stop();
	}

	release {
		vowels[currentVowel].release();
	}

	changeVowel {
		arg newVowel, time;

		if (
			newVowel != currentVowel,
			{
				vowels[currentVowel].setTargetAmplitude(0.01, time);
				currentVowel = newVowel;
				vowels[currentVowel].setAmplitude(0.01);
				vowels[currentVowel].setTargetAmplitude(1, time * 0.8);
				vowels[currentVowel].continue(currentPitch, currentVelocity);
			}, {}
		);
	}
}