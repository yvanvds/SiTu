PitchBufferLocation {
	var <soundName, dictionary;

	*new { | soundName, dictionary |
		^super.newCopyArgs(soundName, dictionary);
	}

	getPitch { | pitch |
		^dictionary[pitch];
	}

	printOn { | stream |
		"name: " + soundName.postln;
		dictionary.do(
			{
				arg item;
				item.postln;
			}
		);
	}
}