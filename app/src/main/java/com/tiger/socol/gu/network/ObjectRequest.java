package com.tiger.socol.gu.network;

public abstract class ObjectRequest<T> extends Request {

    public void request(OnRequestListeren<T> listeren) {
        Agent.getInstance().requestObject(this, listeren);
    }

    public interface OnRequestListeren<T> extends OnRequestErrorListeren {
        void onSuccess(T value);
    }

}
