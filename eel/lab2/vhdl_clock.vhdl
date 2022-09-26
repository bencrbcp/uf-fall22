library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity lab2part1 is
	PORT (
		A, B, clk: in std_logic;
		LED: out std_logic
	);
end entity lab2part1;

architecture arch_lab2part1 of lab2part1 is
begin

	LED <= A and not B;

end architecture arch_lab2part1;
