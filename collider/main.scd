// 0. Run on new Computer
(
Quarks.install("Require");
)

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

~buffer[\yvan][\o].play;
~buffer[\yvan][\o].duration
(
{
	var sig, ptr;
	ptr = Line.ar(0, 3515140, 73.2320833);
	sig = BufRd.ar(1, ~buffer.bufnum, ptr);
}.play;
)

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

(
a = VowelSynth.new(\yvan, \o);
a.prCalculatePitch(54.5);
a.start(52);
a.start(54);
a.start(54.5);
a.continue(\52);
a.continue(\50);
a.release();

a.stop();
)

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
