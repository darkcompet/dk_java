import tool.compet.core.DkLogs;

public class Test {
	private void start() throws Exception {
		double n = 0.81;
		double low = 0;
		double high = n;
		double mid = 1;
		int stepCount = 0;

		while (low < high) {
			mid = (low + high) / 2.0;
			double candidate = mid * mid;

			if (candidate == n) {
				System.out.println("Found ans");
				break;
			}
			else if (candidate > n) {
				high = mid;
				++stepCount;
			}
			else {
				low = mid;
				++stepCount;
			}
		}

		DkLogs.debug(this, "Mid: %f, stepCount: %d, low: %f, high: %f", mid, stepCount, low, high);
	}

	public static void main(String[] args) {
		try {
			new Test().start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
