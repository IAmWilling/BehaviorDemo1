package com.zhyx.behaviordemo1.meituan;

public class MessageEvent {
    private boolean flag;

    public MessageEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
