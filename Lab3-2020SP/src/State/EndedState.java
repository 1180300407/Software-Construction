package State;

import common.CommonPlanningEntry;

public class EndedState implements EntryState{
	@Override
	public String getStateName() {
		return "Ended";
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�����,����ʧ��!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�����,����ʧ��!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�����,�����ظ�����!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�����,ȡ��ʧ��!");
	}
}
