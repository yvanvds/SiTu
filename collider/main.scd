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


// sandbox

v = VocalistController.new(\yvan);
v.setBpm(70);
v.setStepsPerBeat(2);

~buffer[\yvan][\o].play;
~buffer[\yvan][\o].duration


s.sampleRate

MIDIClient.init;
MIDIIn.connectAll;

(
a = VowelSynth.new(\yvan, \o, ~out);

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


a = Vocalist.new(\yvan, ~out);


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
b.start(\40);
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