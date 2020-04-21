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

~buffer;

s.sampleRate



(
var vw = VowelSynth.new(\yvan, \o);
vw.start(\52);
)



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
