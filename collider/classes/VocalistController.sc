VocalistController {
	var vocalist;
	var singerID;
	var clock;

	var currentBpm = 60;
	var stepsPerBeat = 4;
	var beatsPerBar = 4;
	var currentBeat = 0;
	var currentStep = -1;

	var timePattern, pitchPattern, ampPattern, vowelPattern;
	var timeToNextNote = 0;
	var ampMultiplier = 1;

	var notesInScale;

	*new {
		arg singer, out, pan;
		^super.new.init(singer, out, pan);
	}

	init {
		arg s, o, p;

		vocalist = Vocalist.new(s, o, p);
		singerID = s;
		notesInScale = Array.new();
		clock = TempoClock(currentBpm / 60 * stepsPerBeat, 0, ~startTime);

		clock.schedAbs(
			clock.beats.ceil,
			{
				arg beat;

				currentStep = currentStep + 1;

				if (
					currentStep >= stepsPerBeat,
					{
						currentStep = 0;
						currentBeat = currentBeat + 1;

						if (
							currentBeat >= beatsPerBar,
							{ currentBeat = 0; }, {}
						);
					},
					{}
				);

				this.prEvaluateStatus();
				1
			}
		);
	}


	setBpm {
		arg bpm;

		currentBpm = bpm;
		clock.tempo = currentBpm / 60 * stepsPerBeat;
	}

	setStepsPerBeat {
		arg number;

		stepsPerBeat = number;
		clock.tempo = currentBpm / 60 * stepsPerBeat;
	}

	setModus {
		arg scale, base;
		var min, max;

		min = ~singers[singerID][\min];
		max = ~singers[singerID][\max];

		notesInScale = Array();
		forBy(base, max, 12, {
			arg i;
			scale.degrees.do({
				arg j;

				var tone = i + j;
				if((tone >= min) && (tone <= max),
					{
						notesInScale = notesInScale.add(tone);
					}, {}
				);
			});
		});
		("scale: "++notesInScale).postln;
	}

	setTimePattern {
		arg pattern;
		timePattern = pattern.asStream;
	}

	setPitchPattern {
		arg pattern;
		pitchPattern = pattern.asStream;
	}

	setAmpPattern {
		arg pattern;
		ampPattern = pattern.asStream;
	}

	setVowelPattern {
		arg pattern;
		vowelPattern = pattern.asStream;
	}

	setAmpMultiplier {
		arg value;
		ampMultiplier = value;
	}

	prEvaluateStatus {
		var pitch;
		timeToNextNote = timeToNextNote - 1;

		if (
			timeToNextNote <= 0,
			{
				if (
					pitchPattern == nil,
					{ pitch = \stop; },
					{ pitch = pitchPattern.next; }
				);

				case
				{ pitch.isNumber } {
					("pitch: "++pitch).postln;
					if (pitch >= 0 && pitch < notesInScale.size,
						{
							this.prPlay(notesInScale[pitch]);
						},{}
					);
				}
				{ pitch == \stop } { vocalist.stop(); }
				{ pitch == \release } { vocalist.release(); };

				if (
					timePattern == nil,
					{ timeToNextNote = 1; },
					{ timeToNextNote = timePattern.next; }
				);
			},{}
		);
	}

	prPlay {
		arg pitch;
		var amplitude;
		var vowel;

		if (ampPattern == nil, { amplitude = 60; }, { amplitude = ampPattern.next; });
		amplitude = (amplitude * ampMultiplier).clip(5, 120);

		if (vowelPattern == nil, { vowel = \o; }, { vowel = vowelPattern.next; });
		("playing pitch "++pitch++" amp "++amplitude++" vowel "++vowel).postln;
		vocalist.play(pitch, amplitude, vowel);
	}
}