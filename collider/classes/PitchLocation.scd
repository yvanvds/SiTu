PitchLocation {
	var <pitch, <start, <length;

	*new { | pitch, start, length |
		^super.newCopyArgs(pitch, start, length);
	}

	printOn { | stream |
		stream << "PitchEntry(pitch: " << pitch << ", start: " << start << ", length: " << length << ")";
	}
}