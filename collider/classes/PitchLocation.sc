PitchLocation {
	var <pitch, <start, <end;

	*new { | pitch, start, end |
		^super.newCopyArgs(pitch, start, end);
	}

	printOn { | stream |
		stream << "PitchEntry(pitch: " << pitch << ", start: " << start << ", end: " << end << ")";
	}
}