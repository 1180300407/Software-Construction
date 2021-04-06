package State;

import common.CommonPlanningEntry;

public class RunningState implements EntryState{
	@Override
	public String getStateName() {
		return "Running";
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�����,�����ظ�����!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ�����������,����ʧ��!");
	}
	
	/**
	 * �趨״̬Ϊ����״̬
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		cpe.setState(new EndedState());
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ�����������,ȡ��ʧ��!");
	}
}
