// 0. Run on new Computer
(
Quarks.install("Require");
)

ServerOptions.devices

// 1. load Project classes
(
s.quit;
~classPath = PathName(thisProcess.nowExecutingPath).parentPath++"classes/";
LanguageConfig.addIncludePath(~classPath);
thisProcess.recompile;
)

// 2. init project
(
Require("startup");
~startup.value;
~s.meter;
~s.plotTree;
)

~singers[\yvan][\max].postln


// sandbox

a = VocalistGroup.new(\yvan, 4, -0.8, 0.8);
a.setModus(Scale.minor, 7);
a.setPitchPattern(Pseq([4, 0], inf));
a.setVowelPattern(Pseq([\o, \a], inf));
a.setTimePattern(Pseq([4, 4], inf));
a.setTimePattern(Pseq([2, 2, 5, Prand([7, 8, 8,8 ,8], 1)], inf));
a.setAmpMultiplier(0.1);

b = VocalistGroup.new(\yvan, 4, -0.8, 0.8);
b.setModus(Scale.chromatic, 0);
b.setPitchPattern(Prand([20, 21, 22, 24], inf));
a.setPitchPattern(nil);
b.setTimePattern(Pseq([16, 18], inf));
b.setVowelPattern(Pseq([\o, \a], inf));
b.setAmpMultiplier(0.1);

a.setAmpMultiplier(0.8);

(
a.vocalists.do({
	arg item, i;
	item.strategy.setVelocity(100, 127);
});
)

a.setBpm(50);

a.strategy.setLength(1, 2);
a.reverb.level = 0;
a.reverb.levelTo(1.8, 10);

v.setBpm(70);
v.setStepsPerBeat(2);

~buffer[\yvan][\o].play;
~buffer[\yvan][\o].duration


s.sampleRate

MIDIClient.init;
MIDIIn.connectAll;

(
a = VowelSynth.new(\yvan, \o);

MIDIdef.noteOn(\noteOnTest, {
	arg vel, nn, chan, src;
	a.play(nn, vel);
});


MIDIdef.noteOff(\noteOffTest, {
	arg vel, nn;
	//a.stop();
});
)

a.release();
a.stop();


a = Vocalist.new(\yvan);


a.start(62, 100, \o);
a.start(59, 100, \a);
a.changeVowel(\o, 3);
a.changeVowel(\a, 2);
a.start(54.5, 100);
a.continue(50, 100, \o);
a.continue(60, 100, \o);
a.release();
a.stop();

(
b = VowelSynth.new(\yvan, \o);
b.start(40, 100);
b.stop();
)

(
c = VowelSynth.new(\yvan, \o);
c.start(\59);
c.stop();
)

52.asSymbol

\exponential.postln;


~makeEvents = {
	MIDIIn.connectAll;
	e = Dictionary.new;
	e.add(\event1 -> {"event1".postln;});
	e.add(\event2 -> {"event2".postln;});
	e.add(\event3 -> {"event3".postln;});

	MIDIdef.cc(\controller, {nil});
};


n = NetAddr("127.0.0.1", NetAddr.langPort);
o = OSCFunc.newMatching({
	arg msg, time;
	"got it".postln;
}, '/tr', n);

m = NetAddr("127.0.0.1", NetAddr.langPort);

m.sendMsg("/tr", "hello");



var test;
f = e.getPitch(\40);
f.start.postln;

(
SynthDef("help-Dust", { arg out=0;
    Out.ar(out,
        Dust.ar(XLine.kr(20000, 2, 10), 0.5)
    )
}).play;
)

(
~def = SynthDef(
	"TestLine", {
		arg start, end, duration, t_trig = 1, run = 1;
		var line;

		line = Sweep.ar(t_trig, run /duration).linexp(0.0, 1, start, end, \minmax);
		line.poll;
	}
).add;
)

a = Scale.ionian;

    p = Pbind(
        \degree, Pseq([0, 1, 2, 3, 4, 5, 6, 7, 6, 5, 4, 3, 2, 1, 0, \rest], inf),
        \scale, Pfunc({ a }, inf),
        \dur, 0.25
    );

    q = p.stop;

(
t = Synth.new(
	\TestLine,
	[
		\start, 0.01,
		\end, 1,
		\duration, 2,
	]
);
)

t.set(\start, 1, \end, 0.01, \duration, 10, \t_trig, 1);
t.set(\start, 0.01, \end, 1, \duration, 10, \t_trig, 1);
t.set(\run, 0);
t.set(\run, 1);
t.set(\t_trig, 1);
t.free;

Quarks.gui