VocalistController {
	var vocalist;
	var clock;

	var currentBpm = 60;
	var stepsPerBeat = 4;
	var beatsPerBar = 4;
	var currentBeat = 0;
	var currentStep = -1;

	*new {
		arg singer;
		^super.new.init(singer);
	}

	init {
		arg s;

		vocalist = Vocalist(s);

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

	prEvaluateStatus {
		if (
			(currentBeat == 0) && (rrand(1, 10) > 5),
			{
				vocalist.play(rrand(40,65), rrand(60, 127));
			}, {}
		);

		if (
			(currentBeat == 3) && (rrand(1, 10) > 8),
			{
				vocalist.release();
			}, {}
		);
	}
}