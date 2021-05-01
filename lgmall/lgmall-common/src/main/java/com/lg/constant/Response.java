//package com.lg.constant;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Response extends HashMap<String,Object> {
//    private static final long serialVersionUID = 1L;
//
//    public Response setData(Object data) {
//        put("data",data);
//        return this;
//    }
//
//    //利用fastjson进行反序列化
//    public <T> T getData(TypeReference<T> typeReference) {
//        Object data = get("data");	//默认是map
//        String jsonString = JSON.toJSONString(data);
//        T t = JSON.parseObject(jsonString, typeReference);
//        return t;
//    }
//
//    //利用fastjson进行反序列化
//    public <T> T getData(String key) {
//        Object data = get(key);	//默认是map
//        String jsonString = JSON.toJSONString(data);
//        T t = JSON.parseObject(jsonString, new TypeReference<T>(){});
//        return t;
//    }
//
//    public Response() {
//        put("code", 0);
//        put("msg", "success");
//    }
//
//    public static Response error() {
//        return error(Result.WEB_UNKOWN.getCode(), "未知异常，请联系管理员");
//    }
//
//    public static Response error(String msg) {
//        return error(Result.WEB_400.getCode(), msg);
//    }
//
//    public static Response error(int code, String msg) {
//        Response r = new Response();
//        r.put("code", code);
//        r.put("msg", msg);
//        return r;
//    }
//
//    public static Response ok(String msg) {
//        Response r = new Response();
//        r.put("msg", msg);
//        return r;
//    }
//
//    public static Response ok(Map<String, Object> map) {
//        Response r = new Response();
//        r.putAll(map);
//        return r;
//    }
//
//    public static Response ok() {
//        return new Response();
//    }
//
//    public Response put(String key, Object value) {
//        super.put(key, value);
//        return this;
//    }
//
//    public Integer getCode() {
//
//        return (Integer) this.get("code");
//    }
//
//
//}
