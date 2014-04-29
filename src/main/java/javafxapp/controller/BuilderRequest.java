package javafxapp.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javafxapp.adapter.domain.Adapter;
import javafxapp.adapter.fns.Pojo;
import javafxapp.crypto.WSSTool;
import javafxapp.utils.Mapper;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderRequest {


    private static Mapper innerMapper = new Mapper();
    private static Configuration cfg;

   /* public static Adapter createAdapter() throws IOException {
        Adapter nameAdapter = new Adapter();
        nameAdapter.setRequest(IOUtils.toString(BuilderRequest.class.getResourceAsStream("javafxapp.nameAdapter.request_07FL_short.ftl"), "UTF-8"));

        return nameAdapter;
    }*/

    public static List<Adapter> buildRequestByTemplateFns(HashMap<String, List<Pojo>> hashMap)  {
        cfg = new Configuration();
        List<Adapter> adapters = new ArrayList<>();
        for(Map.Entry<String, List<Pojo>> entry : hashMap.entrySet()) {
            try {
                Template template = cfg.getTemplate("src/main/java/javafxapp/adapter/fns/request_"+ entry.getKey() +".ftl");

        /*SmevFields SmevFields = (SmevFields) innerMapper.fillSmevPojo(null, new Pojo());
        fns.setIsInn("on");
        List<String> listInn = new ArrayList<>();
        listInn.add("500100732259");
        fns.setInn(listInn);*/
                for (Pojo pojo : entry.getValue()) {
                    build("fns", adapters, template, pojo);
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }

        }
        return adapters;
    }

    public static List<Adapter> buildRequestByTemplateMvd(List<javafxapp.adapter.mvd.Pojo> listMvd)  {
        cfg = new Configuration();
        List<Adapter> adapters = new ArrayList<>();
        try {
            Template template = cfg.getTemplate("src/main/java/javafxapp/adapter/mvd/request.ftl");
            for (javafxapp.adapter.mvd.Pojo pojo : listMvd) {
                build("mvd", adapters, template, pojo);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }

        return adapters;
    }

    private static void build(String foiv, List<Adapter> adapters, Template template, Object pojo) throws IllegalAccessException, TemplateException, IOException {
        StringWriter out;
        Adapter adapter;Field[] fields = pojo.getClass().getFields();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Field f : fields) {
            map.put(f.getName(), f.get(pojo));
        }
        out = new StringWriter();
        template.process(map, out);
        String result = new String(out.getBuffer().toString().getBytes("UTF-8"), "UTF-8");
        out.flush();
        out.close();
        result = WSSTool.signSoapRequest(result);
        adapter = new Adapter();
        if (foiv.equals("fns")) {
            Pojo fns = (Pojo) pojo;
            adapter.setNumReq(fns.getRowNum());
            adapter.setId210fz(fns.getId210fz());
        }
        if (foiv.equals("mvd")) {
            javafxapp.adapter.mvd.Pojo mvd = (javafxapp.adapter.mvd.Pojo) pojo;
            adapter.setNumReq(mvd.getRowNum());
            adapter.setId210fz(mvd.getId210fz());
        }

        adapter.setRequestXml(result);
        adapters.add(adapter);
    }

    public static Object fillSmevFieldsDefault(String foiv) throws Exception {
        Class<?> pojoObject = Class.forName("javafxapp.adapter." + foiv + ".Pojo");
        Object pojo = pojoObject.newInstance();
        pojo = innerMapper.fillSmevPojo(null, pojo, foiv);
        return pojo;
    }


}