package API;

public class PlanningEntryAPIsSecondImplTest extends PlanningEntryAPIsTest{

	@Override
	public PlanningEntryAPIs getAPI() {
		return new PlanningEntryAPIsSecondImpl();
	}
}
