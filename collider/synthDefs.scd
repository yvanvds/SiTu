~createSynths = {
	SynthDef(
		\vowel,
		{
			arg amp=1, buf, start, end, duration;
			var sig, ptr, env, envGen, scaledStart, scaledEnd, scaledDuration;

			// scaledStart = start * BufRateScale.kr(buf);
			// Poll.kr(Impulse.kr(0), scaledStart, \start);
			// scaledEnd = end * BufRateScale.kr(buf);
			// Poll.kr(Impulse.kr(0), scaledStart, \end);
			// scaledDuration = (scaledEnd - scaledStart) / BufSampleRate.kr(buf) / BufRateScale.kr(buf);
			// Poll.kr(Impulse.kr(0), scaledDuration, \duration);

			env = Env([0, 1, 1, 0], [0.05, duration - 0.1, 0.05]);
			envGen = EnvGen.kr(env);
			// Poll.kr(Impulse.kr(40), envGen, \env);

			ptr = Line.ar(start, end, duration, doneAction: 2);
			sig = BufRd.ar(1, buf, ptr);
			sig = sig * amp * envGen;

			Out.ar(~bus[\reverb], sig);

			//timer = Line.kr(-1, 0.01, duration - 0.1);
			//timer.poll;

			//SendTrig.kr(Line.kr(-1, 0.01, duration - 0.1), 0, 0.8);
	}).add;

	SynthDef(
		\reverb,
		{
			arg in, predelay=0.1, revtime=1.8, lpf=4500, mix=0.15, amp=1, out=0;

			var dry, wet, temp, sig;

			dry = In.ar(in, 2);
			temp = In.ar(in, 2);
			wet = 0;
			temp = DelayN.ar(temp, 0.2, predelay);
			16.do {
				temp = AllpassN.ar(temp, 0.05, {Rand(0.001, 0.05)}!2, revtime);
				temp = LPF.ar(temp, lpf);
				wet = wet + temp;
			};

			sig = XFade2.ar(dry, wet, mix*2-1, amp);
			Out.ar(out, sig);
		}
	).add;

	"... Synths Created".postln;
}