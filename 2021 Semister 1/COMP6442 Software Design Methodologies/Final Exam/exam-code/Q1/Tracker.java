import java.util.List;
import java.util.Map;

public interface Tracker {

	public void trace(long x, long y);

	public int getCount();

	public Map<Integer, List<Long>> getTraces();
}
