~createReverb = {

	~reverbChannel = MixerChannel.new("reverb channel", ~s, 2, 2, outbus:~master);

	~reverb = ~reverbChannel.playfx({ |out|
		var numChan, maxDelay, preDelay, decay, numRefl, random;
		var sig, new, dlys;
		#numChan, maxDelay, preDelay, decay, numRefl, random = #[2, 0.881, 0.08, 4, 8, 0.1];
		sig = In.ar(out, numChan);
		new = sig;
		dlys = Array.fill(numRefl, {
			new = AllpassN.ar(new, maxDelay,
				Array.fill(numChan, { random.rand }) + preDelay, decay);
		});
		Mix.ar(dlys * Array.series(numRefl, 1, (numRefl+1).reciprocal.neg))
	});
	"... Reverb Channel Created".postln;
}
