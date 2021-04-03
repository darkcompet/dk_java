/*
 * Copyright (c) 2017-2020 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.annotation.Nullable;

/**
 * This class, provides common basic operations for math.
 */
public final class DkMaths {
	// region Java core

	public static boolean parseBoolean(@Nullable String text) {
		return "1".equals(text) || "true".equals(text);
	}

	public static short parseShort(@Nullable String text) {
		return (short) parseInt(text);
	}

	public static int parseInt(@Nullable String text) {
		if (text == null) {
			return 0;
		}

		int result = 0;
		int index = 0;
		final int N = text.length();

		while (index < N && text.charAt(index) == '-') {
			++index;
		}
		boolean minus = ((index & 1) == 1);
		char ch;

		while (index < N && '0' <= (ch = text.charAt(index++)) && ch <= '9') {
			result = (result << 3) + (result << 1) + (ch - '0');
		}
		return minus ? -result : result;
	}

	public static long parseLong(@Nullable String text) {
		if (text == null) {
			return 0L;
		}

		long result = 0L;
		int index = 0;
		final int N = text.length();

		while (index < N && text.charAt(index) == '-') {
			++index;
		}
		boolean minus = ((index & 1) == 1);
		char ch;

		while (index < N && '0' <= (ch = text.charAt(index++)) && ch <= '9') {
			result = (result << 3) + (result << 1) + (ch - '0');
		}
		return minus ? -result : result;
	}

	public static float parseFloat(String text) {
		try {
			return Float.parseFloat(text);
		}
		catch (Exception ignore) {
			return 0f;
		}
	}

	public static double parseDouble(String text) {
		try {
			return Double.parseDouble(text);
		}
		catch (Exception ignore) {
			return 0.0;
		}
	}

	public static int min(int... args) {
		int min = args[0];

		for (int x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static long min(long... args) {
		long min = args[0];

		for (long x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static float min(float... args) {
		float min = args[0];

		for (float x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static double min(double... args) {
		double min = args[0];

		for (double x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static int max(int... args) {
		int max = args[0];

		for (int x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static long max(long... args) {
		long max = args[0];

		for (long x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static float max(float... args) {
		float max = args[0];

		for (float x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static double max(double... args) {
		double max = args[0];

		for (double x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static double sin(double degrees) {
		return Math.sin(Math.PI * degrees / 180.0);
	}

	public static double cos(double degrees) {
		return Math.cos(Math.PI * degrees / 180.0);
	}

	public static double tan(double degrees) {
		return Math.tan(Math.PI * degrees / 180.0);
	}

	public static int fastPow(int x, int n) {
		return fastPow(x, n, 0);
	}

	/**
	 * If mod > 0 then return x^n % mod. Otherwise return x^n;
	 */
	public static int fastPow(int x, int n, int mod) {
		if (n == 0) {
			return x == 0 ? 0 : 1;
		}
		if (n < 0) {
			return 0;
		}

		int result = 1;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				if (mod > 0) {
					result = result * x % mod;
				}
				else {
					result *= x;
				}
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				if (mod > 0) {
					x = x * x % mod;
				}
				else {
					x *= x;
				}
			}
		}

		return result;
	}

	public static long fastPow(long x, int n) {
		return fastPow(x, n, 0);
	}

	public static long fastPow(long x, int n, long mod) {
		if (n == 0) {
			return 1;
		}
		if (n < 0) {
			return 0;
		}

		long res = 1;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				if (mod > 0) {
					res = res * x % mod;
				}
				else {
					res *= x;
				}
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				if (mod > 0) {
					x = x * x % mod;
				}
				else {
					x *= x;
				}
			}
		}

		return res;
	}

	public static float fastPow(float x, int n) {
		if (n == 0) {
			return 1;
		}
		if (n < 0) {
			return 1f / fastPow(x, -n);
		}

		float res = 1f;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				res *= x;
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				x *= x;
			}
		}

		return res;
	}

	public static double fastPow(double x, int n) {
		if (n == 0) {
			return 1;
		}
		if (n < 0) {
			return 1.0 / fastPow(x, -n);
		}

		double res = 1;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				res *= x;
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				x *= x;
			}
		}

		return res;
	}

	/**
	 * @return degrees in range [-180, 180].
	 */
	public static double normalizeAngle(double degrees) {
		if (degrees > 360 || degrees < -360) {
			degrees %= 360;
		}
		if (degrees > 180) {
			degrees -= 360;
		}
		if (degrees < -180) {
			degrees += 360;
		}
		return degrees;
	}

	/**
	 * @return nomarlized value in range [0, 1].
	 */
	public static double normalize(double value, double from, double to) {
		double min = Math.min(from, to);
		double max = Math.max(from, to);

		return (value - min) / (max - min);
	}

	/**
	 * @return given value if it is in range [min, max]. Otherwise return min or max.
	 */
	public static double clamp(double value, double min, double max) {
		return value < min ? min : Math.min(value, max);
	}

	// More correct when computes root of int value.
	public static int intSqrt(int n) {
		int root = (int) Math.sqrt(n + 0.5);
		while ((long) root * root > n) --root;
		return root;
	}

	// More correct when computes root of long value.
	public static long longSqrt(long n) {
		long root = (long) Math.sqrt(n + 0.5);
		while (root * root > n) --root;
		return root;
	}

	// endregion Java core
}
