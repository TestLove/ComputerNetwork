package com.testlove.network.analyse;



public abstract class Statistics {
	
	private StatisticsManager sm;
	
	Class<? extends Protocol> detailProtocolClass = null;

	
	public Statistics() {
		sm = StatisticsManager.newInstance();
		sm.register(this);
	}

	public abstract void clean();

	public Class<? extends Protocol> getDetailProtocolClass() {
		return detailProtocolClass;
	}

	public void setDetailProtocolClass(Class<? extends Protocol> detailProtocolClass) {
		this.detailProtocolClass = detailProtocolClass;
	}
	
	

}
