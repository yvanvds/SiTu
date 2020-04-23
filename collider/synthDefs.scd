~createSynths = {
	SynthDef(
		\vowel,
		{
			arg amp=0.3, buf, start, speed, gate = 0, attack = 0.05, decay = 0.05, out = 0;
			var sig, envGen;

			envGen = EnvGen.kr(Env.asr(attack, 1, decay, 'linear') , gate, doneAction: 2);
			// Poll.kr(Impulse.kr(40), envGen, \env);

			// ptr = Line.ar(start, end, duration, doneAction: 2);
			// sig = BufRd.ar(1, buf, ptr);
			sig = PlayBuf.ar(1, buf, speed, startPos: start, doneAction: 2);
			sig = sig * amp * envGen;

			Out.ar(out, sig);
	}).add;

	SynthDef(
		\reverb,
		{
			arg in, predelay=0.1, revtime=0.8, lpf=4500, mix=0.15, amp=1, out=0;

			var dry, wet, temp, sig;

			dry = In.ar(in, 2);
			temp = In.ar(in, 2);
			wet = 0;
			temp = DelayN.ar(temp, 0.2, predelay);
			8.do {
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