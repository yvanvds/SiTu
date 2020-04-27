VocalistGroup {
	var channel;
	var <vocalists;
	var <reverb;

	*new {
		arg singer, amount, minPan, maxPan;
		^super.new.init(singer, amount, minPan, maxPan);
	}

	init {
		arg singer, amount, minPan, maxPan;

		var range, panStep;

		range = maxPan - minPan;
		("range: "++range).postln;

		panStep = range / (amount - 1);
		("pan step: "++panStep).postln;

		channel = MixerChannel(\vocalGroup++singer, ~s, 2, 2, level: 1, outbus: ~master);
		reverb = channel.newPostSend(~reverbChannel, 0.41);

		vocalists = Array.fill(
			amount,
			{
				arg i;
				VocalistController.new(singer, channel, minPan + (panStep * i));
			}
		);
	}

	setBpm {
		arg bpm;

		vocalists.do({
			arg item, i;
			item.setBpm(bpm);
		});
	}

	setStepsPerBeat {
		arg number;

		vocalists.do({
			arg item, i;
			item.setStepsPerBeat(number);
		});
	}

	setModus {
		arg scale, base;

		vocalists.do({
			arg item, i;
			item.setModus(scale, base);
		});
	}

	setTimePattern {
		arg pattern;
		vocalists.do({
			arg item, i;
			item.setTimePattern(pattern);
		});
	}

	setPitchPattern {
		arg pattern;
		vocalists.do({
			arg item, i;
			item.setPitchPattern(pattern);
		});
	}

	setAmpPattern {
		arg pattern;
		vocalists.do({
			arg item, i;
			item.setAmpPattern(pattern);
		});
	}

	setVowelPattern {
		arg pattern;
		vocalists.do({
			arg item, i;
			item.setVowelPattern(pattern);
		});
	}

	setAmpMultiplier {
		arg value;
		vocalists.do({
			arg item, i;
			item.setAmpMultiplier(value);
		});
	}


}