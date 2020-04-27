Require("buffers");
Require("synthDefs");
Require("reverb");
Require("SingersLookupTable");

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
	~startTime = Main.elapsedTime.ceil;


	VowelSynth.initClass();
	"... Variables Set".postln;

	ServerQuit.add(~cleanup);
	"... Server Functions Registered".postln;

	"... Starting Server".postln;
	~s.waitForBoot({
		~s.sync;
		~loadBuffers.value;

		~s.sync;
		ServerTree.add(~createSynths);
		ServerTree.add(~createSingersLookupTable);
		~s.sync;
		// ServerTree.add(~makeNodes);
		~s.freeAll;

		~s.sync;
		~master = MixerChannel(\master, ~s, 2, 2, level:1);

		~createReverb.value;

		"... Ready to Play".postln;
	});
};
