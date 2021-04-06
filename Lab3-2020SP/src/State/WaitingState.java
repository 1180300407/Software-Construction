package State;

import common.CommonPlanningEntry;

public class WaitingState implements EntryState{
	@Override
	public String getStateName() {
		return "Waiting";
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ�����δ����,����ʧ��!");
	}
	
	/**
	 * ����״̬Ϊ����״̬
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		cpe.setState(new AllocatedState());
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ�����δ����,����ʧ��!");
	}
	
	/**
	 * ����״̬Ϊȡ��״̬
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		cpe.setState(new CancelledState());
	}
}
