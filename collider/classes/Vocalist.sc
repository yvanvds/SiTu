Vocalist {
	var vowels;
	var singer, out;

	var currentVowel;

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
			]
		);
		currentVowel = \o;
	}

	play {
		arg pitch, velocity, vowel;

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
				vowels[currentVowel].play(pitch, velocity);
			}
		);
	}

	start {
		arg pitch, velocity, vowel;

		if (
			vowel != currentVowel and: vowels[currentVowel].isPlaying,
			{
				vowels[currentVowel].stop();
			},{}
		);
		currentVowel = vowel;
		vowels[currentVowel].start(pitch, velocity);
	}

	continue {
		arg pitch, velocity, vowel;

		if (
			vowel != currentVowel and: vowels[currentVowel].isPlaying,
			{
				vowels[currentVowel].stop();
			},{}
		);
		currentVowel = vowel;
		vowels[currentVowel].continue(pitch, velocity);
	}

	stop {
		vowels[currentVowel].stop();
	}

	release {
		vowels[currentVowel].release();
	}
}