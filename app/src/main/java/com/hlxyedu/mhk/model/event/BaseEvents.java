package com.hlxyedu.mhk.model.event;

/**
 * 事件集中定义
 * @author weidingqiang
 */
public class BaseEvents {

	public static final String NOTICE = "NOTICE";
	
	private Object data;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	private String type;

	private int value;

	public String getType() {
		return type;
	}

    public void setType(String type) {
        this.type = type;
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public BaseEvents(String type, int value, Object data) {
		super();
		this.type = type;
		this.value = value;
		this.data = data;
	}

	public BaseEvents(String type, int value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public BaseEvents(String type, Object data) {
        super();
        this.type = type;
        this.data = data;
    }
	
}
