import java.util.List;
import java.util.Map;

public class EmptyTracker implements Tracker {

	@Override
	public void trace(long x, long y) {
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Map<Integer, List<Long>> getTraces() {
		return null;
	}
}
