Require("busses");
Require("buffers");
Require("synthDefs");
Require("nodes");

~cleanup = {
	s.newBusAllocators;
	ServerBoot.removeAll;
	ServerTree.removeAll;
	ServerQuit.removeAll;
};

~startup = {
	~s = Server.local;
	~s.options.outDevice_(
		//"MME : Speakers (Realtek High Definiti"
		"MME : Headphones (UE MEGABOOM Stereo)"
	);
	~s.options.numOutputBusChannels_(2);
	~s.options.sampleRate_(44100);
	~s.options.memSize_(2.pow(20)); // gives about a gig of real time memory
	"... Server Options Set".postln;


	~out = 0;


	VowelSynth.initClass();
	"... Variables Set".postln;

	ServerBoot.add(~makeBusses);
	ServerQuit.add(~cleanup);
	"... Server Functions Registered".postln;

	"... Starting Server".postln;
	~s.waitForBoot({
		~s.sync;
		~loadBuffers.value;

		~s.sync;
		ServerTree.add(~createSynths);
		~s.sync;
		ServerTree.add(~makeNodes);
		~s.freeAll;

		~s.sync;
		~master = MixerChannel(\master, ~s, 0, 2, level:1);

		"... Ready to Play".postln;
	});
};
