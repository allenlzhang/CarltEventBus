package com.carlt.carlteventbus;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2019/2/28 16:32
 */
class MesageEvent {
    public String msg;
    public String mString;

    public MesageEvent(String msg, String string) {
        this.msg = msg;
        mString = string;
    }

    @Override
    public String toString() {
        return "MesageEvent{" +
                "msg='" + msg + '\'' +
                ", mString='" + mString + '\'' +
                '}';
    }
}
