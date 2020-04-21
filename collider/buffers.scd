~loadBuffers = {
	var bufferPath = PathName(thisProcess.nowExecutingPath).parentPath++"buffers/";
	~buffer = Dictionary.new;

	PathName(bufferPath).entries.do {
		arg subfolder;
		~buffer.add(
			subfolder.folderName.asSymbol -> Dictionary.new
		);
		subfolder.entries.do {
			arg file;
			if (file.extension == "wav") {
				~buffer[subfolder.folderName.asSymbol].put(
					file.fileNameWithoutExtension.asSymbol,
					Buffer.read(~s, file.fullPath),
				);
			}
		}
	};
	"... Buffers Loaded".postln;
}