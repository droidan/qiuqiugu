package com.tiger.socol.gu.network;

public abstract class FlowRequest<T> extends Request {

    public void request(OnRequestListeren<T> listeren) {
        Agent.getInstance().requestFlow(this, listeren);
    }

    public interface OnRequestListeren<T> extends OnRequestErrorListeren {
        void onSuccess(FlowEntity<T> value);
    }

}
