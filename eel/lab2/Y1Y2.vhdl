library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity Y1 is
	PORT (
		A1, B1, C1, A2, B2, C2: in std_logic;
		Y1, Y2: out std_logic
	);
end entity Y1;

architecture arch_Y1 of Y1 is
begin

	Y1 <= (A1 and B1) or (C1 and A1) or (C1 and B1);
	Y2 <= B2 or (A2 and not C2);

end architecture arch_Y1;
