package com.tiger.socol.gu.network;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Request {

    public abstract String api();

    public abstract void modules(List<Module> ms);

    public abstract void parament(Map<String, String> ps);

    public String URL() {
        return Config.URL;
    }

    public Method method() {
        return Method.POST;
    }

    public void files(Map<String, File> fs) {

    }

}
