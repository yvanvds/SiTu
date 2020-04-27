~createSingersLookupTable = {
	~singers = Dictionary.with(
		*[
			\yvan -> Dictionary.with(
			 *[
				\min->40,
				\max->65,
				\o -> Dictionary.with(
				*[
					\40->PitchLocation.new(40, 2652, 169937),
					\41->PitchLocation.new(41, 185000, 391088),
					\43->PitchLocation.new(43, 412834, 611659),
					\45->PitchLocation.new(45, 634002, 855565),
					\47->PitchLocation.new(47, 873431, 1078077),
					\48->PitchLocation.new(48, 1095474, 1312788),
					\50->PitchLocation.new(50, 1327834, 1541046),
					\52->PitchLocation.new(52, 1577485, 1747472),
					\53->PitchLocation.new(53, 1776859, 1927222),
					\55->PitchLocation.new(55, 1953199, 2133612),
					\57->PitchLocation.new(57, 2173459, 2390000),
					\59->PitchLocation.new(59, 2427549, 2635906),
					\60->PitchLocation.new(60, 2673183, 2850000),
					\62->PitchLocation.new(62, 2861903, 3053124),
					\64->PitchLocation.new(64, 3077858, 3280000),
					\65->PitchLocation.new(65, 3318433, 3471644),
				]),
				\a -> Dictionary.with(
				*[
					\40->PitchLocation.new(40, 1168, 126512),
					\41->PitchLocation.new(41, 165000, 312511),
					\43->PitchLocation.new(43, 345150, 488548),
					\45->PitchLocation.new(45, 512749, 666000),
					\47->PitchLocation.new(47, 682568, 846000),
					\48->PitchLocation.new(48, 871479, 1024000),
					\50->PitchLocation.new(50, 1045268, 1207700),
					\52->PitchLocation.new(52, 1247724, 1400932),
					\53->PitchLocation.new(53, 1418740, 1586660),
					\55->PitchLocation.new(55, 1610000, 1773528),
					\57->PitchLocation.new(57, 1802000, 1978670),
					\59->PitchLocation.new(59, 2005587, 2165625),
					\60->PitchLocation.new(60, 2195695, 2352000),
					\62->PitchLocation.new(62, 2374000, 2516765),
					\64->PitchLocation.new(64, 2534339, 2676657),
					\65->PitchLocation.new(65, 2708449, 2864000),
				]),
			]),
		]);
}