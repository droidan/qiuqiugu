package com.tiger.socol.gu.utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class RealmMannager {

    private Realm realm;
    private static RealmMannager single = null;

    private RealmMannager() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmMannager getInstance() {
        if (single == null) {
            synchronized (RealmMannager.class) {
                if (single == null) {
                    single = new RealmMannager();
                }
            }
        }
        return single;
    }

    /**
     * 保存一个对象
     *
     * @param obj obj
     * @param <E> RealmModel
     */
    public <E extends RealmModel> void syncSava(final E obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(obj);
            }
        });
    }

    public <E extends RealmModel> void asyncSava(final E obj) {
        asyncSava(obj, null);
    }

    public <E extends RealmModel> RealmAsyncTask asyncSava(final E obj, final OnRealmExecuteListener listener) {
        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(obj);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (listener == null)
                    return;
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (listener == null)
                    return;
                listener.onError(error);
            }
        });
    }

    /**
     * 保存一组对象
     *
     * @param list list
     * @param <E>  RealmModel
     */
    public <E extends RealmModel> void syncSavaList(final List<E> list) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(list);
            }
        });
    }

    public <E extends RealmModel> void asyncSavaList(final List<E> list) {
        asyncSavaList(list, null);
    }

    public <E extends RealmModel> RealmAsyncTask asyncSavaList(final List<E> list, final OnRealmExecuteListener listener) {
        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(list);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (listener == null)
                    return;
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (listener == null)
                    return;
                listener.onError(error);
            }
        });
    }

    /**
     * 删除一个对象
     *
     * @param obj obj
     * @param <E> RealmModel
     */
    public <E extends RealmObject> void syncDelete(final E obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                obj.deleteFromRealm();
            }
        });
    }

    public <E extends RealmObject> void asyncDelete(final E obj) {
        asyncDelete(obj, null);
    }

    public <E extends RealmObject> RealmAsyncTask asyncDelete(final E obj, final OnRealmExecuteListener listener) {
        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                obj.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (listener == null)
                    return;
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (listener == null)
                    return;
                listener.onError(error);
            }
        });
    }

    /**
     * 删除所有
     *
     * @param e   class
     * @param <E> RealmModel
     */
    public <E extends RealmModel> void syncDeleteAll(final Class<E> e) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(e);
            }
        });
    }

    public <E extends RealmModel> void asyncDeleteAll(final Class<E> e) {
        asyncDeleteAll(e, null);
    }

    public <E extends RealmModel> RealmAsyncTask asyncDeleteAll(final Class<E> e, final OnRealmExecuteListener listener) {
        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(e);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (listener == null)
                    return;
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (listener == null)
                    return;
                listener.onError(error);
            }
        });
    }

    /**
     * 查询对象列表
     *
     * @param clazz class
     * @param <E>   RealmModel
     * @return List<RealmModel>
     */
    public <E extends RealmModel> List<E> syncFindAll(Class<E> clazz) {
        RealmResults<E> s = realm.where(clazz).findAll();
        List<E> list = new ArrayList<>();
        for (E e : s) {
            list.add(e);
        }
        return list;
    }

    // 请在退出 Activity 或者 Fragment 时移除监听器的注册以避免内存泄漏。
    // result.removeChangeListeners();
    public <E extends RealmModel> RealmResults<E> asyncFindAll(Class<E> clazz, RealmChangeListener<RealmResults<E>> listener) {
        RealmResults<E> s = realm.where(clazz).findAllAsync();
        if (listener != null) {
            s.addChangeListener(listener);
        }
        return s;
    }

    /**
     * 查询第一个
     *
     * @param clazz class
     * @param <E>   RealmModel
     * @return E
     */
    public <E extends RealmModel> E syncFindFrist(Class<E> clazz) {
        return realm.where(clazz).findFirst();
    }

    /**
     * 更新对象
     * 对象必须要有主键(@PrimaryKey), 不然会抛出异常。
     *
     * @param obj obj
     * @param <E> RealmModel
     */
    public <E extends RealmModel> void syncUpdata(final E obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obj);
            }
        });
    }

    public <E extends RealmModel> void asyncUpdate(final E obj) {
        asyncUpdate(obj, null);
    }

    public <E extends RealmModel> RealmAsyncTask asyncUpdate(final E obj, final OnRealmExecuteListener listener) {
        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obj);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (listener == null)
                    return;
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (listener == null)
                    return;
                listener.onError(error);
            }
        });
    }

    public interface OnRealmExecuteListener {

        void onSuccess();

        void onError(Throwable error);
    }

}
