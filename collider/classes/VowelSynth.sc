VowelSynth {
	classvar vowels;
	var singer, vowel;

	var currentPitch;
	var scheduler;

	*new {
		arg singer, vowel;
		^super.newCopyArgs(singer, vowel);
	}

	start {
		arg pitch;
		var buf, pitchData, start, end, duration, scale;

		currentPitch = pitch;
		buf = ~buffer[singer][vowel];
		pitchData = vowels[singer][vowel][pitch];
		scale = buf.sampleRate / ~s.sampleRate;

		start = pitchData.start;
		end = pitchData.end - rrand(48000, 72000);
		duration = ((end * scale) - (start * scale)) / buf.sampleRate / scale;

		Synth.new(\vowel, [\buf, buf.bufnum, \start, start, \end, end, \duration, duration]);

		/*if (
			scheduler != Nil,
			{ "scheduler exists".postln; },
			{
				scheduler = Scheduler(SystemClock);
				"scheduler created".postln;
			}
		);*/
		scheduler.sched(duration - 0.05, { this.continue(); });
	}

	continue {
		"continue called".postln;
	}

	*initClass {
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
			]),
		]);
	}
}

