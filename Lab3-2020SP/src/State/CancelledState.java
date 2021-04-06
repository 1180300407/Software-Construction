package State;

import common.CommonPlanningEntry;

public class CancelledState implements EntryState{
	@Override
	public String getStateName() {
		return "Cancelled";
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void start(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�ȡ��,����ʧ��!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void allocate(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�ȡ��,����ʧ��!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void end(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�ȡ��,����ʧ��!");
	}
	
	/**
	 * �Ƿ�״̬ת��
	 */
	@Override
	public <R> void cancel(CommonPlanningEntry<R> cpe) {
		System.out.println("�üƻ����Ѿ�ȡ��,�����ظ�����!");
	}
}
